package com.hospital.serviceImpl;


import com.hospital.dto.*;
import com.hospital.entity.LabOrder;
import com.hospital.entity.LabReport;
import com.hospital.entity.LabTechnician;
import com.hospital.enums.LabReportStatus;
import com.hospital.enums.NotificationType;
import com.hospital.enums.Role;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.*;
import com.hospital.service.HospitalNotificationService;
import com.hospital.service.LabReportService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;




@Service
@RequiredArgsConstructor
public class LabReportServiceImpl
        implements LabReportService {


    private final HospitalNotificationService hospitalNotificationService;
    private final LabReportRepository labReportRepository;

    private final LabOrderRepository labOrderRepository;

    private final LabTechnicianRepository labTechnicianRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;



    @Override
    @Transactional
    public LabReportResponse uploadReport(
            UploadLabReportRequest request) {


        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        LabTechnician technician =
                labTechnicianRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab Technician not found"
                                ));

        LabOrder labOrder =
                labOrderRepository.findById(
                                request.getLabOrderId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab Order not found"));



        MultipartFile file =
                request.getFile();



        String fileName =
                System.currentTimeMillis()
                        +"_"+file.getOriginalFilename();


        Path uploadPath = Paths.get(uploadDir);
        try {

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }



            Files.copy(
                    file.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );


        }
        catch(Exception e){

            throw new RuntimeException(
                    "File upload failed"
            );

        }



        LabReport report = new LabReport();

        report.setLabOrder(labOrder);

        report.setLabTechnician(technician);

        report.setReport(request.getReport());

        report.setFilePath(
                uploadPath.resolve(fileName).toString()
        );

        report.setStatus(LabReportStatus.UPLOADED);

        report.setCreatedAt(
                LocalDateTime.now()
        );


        report.setUpdatedAt(
                LocalDateTime.now()
        );



        LabReport saved =
                labReportRepository.save(report);


        // =====================================
// Notification to Doctor
// =====================================

        hospitalNotificationService.createNotification(
                labOrder.getAppointment().getDoctor().getId(),
                Role.DOCTOR,
                NotificationType.LAB_REPORT,
                "Lab Report Uploaded",
                "Lab report for patient "
                        + labOrder.getAppointment().getPatient().getFirstName()
                        + " "
                        + labOrder.getAppointment().getPatient().getLastName()
                        + " has been uploaded."
        );

// =====================================
// Notification to Patient
// =====================================

        hospitalNotificationService.createNotification(
                labOrder.getAppointment().getPatient().getId(),
                Role.PATIENT,
                NotificationType.LAB_REPORT,
                "Lab Report Ready",
                "Your lab report for "
                        + labOrder.getLabTest().getTestName()
                        + " is ready."
        );

        return map(saved);

    }





    private LabReportResponse map(
            LabReport report){


        return LabReportResponse.builder()

                .id(report.getId())

                .labOrderId(
                        report.getLabOrder().getId()
                )

                .report(
                        report.getReport()
                )

                .filePath(
                        report.getFilePath()
                )

                .status(
                        report.getStatus()
                )

                .createdAt(
                        report.getCreatedAt()
                )

                .updatedAt(
                        report.getUpdatedAt()
                )

                .build();

    }

    @Override
    public LabReportResponse getReportByLabOrderId(
            Long labOrderId) {


        LabReport report =
                labReportRepository
                        .findByLabOrderId(labOrderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab Report not found"
                                )
                        );


        return map(report);

    }

    @Override
    @Transactional
    public void reviewReport(
            Long reportId,
            LabReportReviewRequest request) {

        LabReport report =
                labReportRepository.findById(reportId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Report not found"));

        report.setDoctorRemarks(
                request.getDoctorRemarks()
        );

        report.setReviewed(true);

        report.setReviewedAt(
                LocalDateTime.now()
        );

        labReportRepository.save(report);

        // ==========================
        // Notification to Patient
        // ==========================

        hospitalNotificationService.createNotification(
                report.getLabOrder()
                        .getAppointment()
                        .getPatient()
                        .getId(),
                Role.PATIENT,
                NotificationType.LAB_REPORT,
                "Lab Report Reviewed",
                "Your lab report for "
                        + report.getLabOrder()
                        .getLabTest()
                        .getTestName()
                        + " has been reviewed by Dr. "
                        + report.getLabOrder()
                        .getAppointment()
                        .getDoctor()
                        .getFirstName()
                        + " "
                        + report.getLabOrder()
                        .getAppointment()
                        .getDoctor()
                        .getLastName()
                        + "."
        );
    }
    @Override
    public List<LabReportResponse> getReportsByDoctor(Long doctorId) {

        List<LabReport> reports =
                labReportRepository.findByLabOrderAppointmentDoctorId(doctorId);

        List<LabReportResponse> response = new ArrayList<>();

        for (LabReport report : reports) {
            response.add(map(report));
        }

        return response;
    }

    @Override
    public List<LabReportResponse> getReportsByPatient(Long patientId) {

        List<LabReport> reports =
                labReportRepository.findByLabOrderAppointmentPatientId(patientId);

        List<LabReportResponse> response = new ArrayList<>();

        for (LabReport report : reports) {
            response.add(map(report));
        }

        return response;
    }

    @Override
    public List<LabReportResponse> getReportsByLabTechnician(Long labTechnicianId) {

        List<LabReport> reports =
                labReportRepository.findByLabTechnicianId(labTechnicianId);

        List<LabReportResponse> response = new ArrayList<>();

        for (LabReport report : reports) {
            response.add(map(report));
        }

        return response;
    }


    @Override
    public Resource downloadReport(Long reportId) {

        LabReport report = labReportRepository.findById(reportId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lab Report not found"));

        try {

            Path path = Paths.get(report.getFilePath());

            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found");
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid file path");
        }
    }

}
package com.hospital.serviceImpl;


import com.hospital.dto.*;
import com.hospital.entity.LabOrder;
import com.hospital.entity.LabReport;
import com.hospital.entity.LabTechnician;
import com.hospital.enums.LabReportStatus;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.*;
import com.hospital.service.LabReportService;


import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


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



        try {


            Path uploadPath =
                    Paths.get(uploadDir);



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

        report.setFilePath(uploadDir + fileName);

        report.setStatus(LabReportStatus.UPLOADED);

        report.setCreatedAt(
                LocalDateTime.now()
        );


        report.setUpdatedAt(
                LocalDateTime.now()
        );



        LabReport saved =
                labReportRepository.save(report);



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
    public void reviewReport(
            Long reportId,
            LabReportReviewRequest request){


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

}
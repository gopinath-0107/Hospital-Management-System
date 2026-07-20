package com.hospital.serviceImpl;


import com.hospital.dto.*;
import com.hospital.entity.LabOrder;
import com.hospital.entity.LabReport;
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



@Service
@RequiredArgsConstructor
public class LabReportServiceImpl
        implements LabReportService {



    private final LabReportRepository labReportRepository;

    private final LabOrderRepository labOrderRepository;



    @Value("${file.upload-dir}")
    private String uploadDir;



    @Override
    @Transactional
    public LabReportResponse uploadReport(
            UploadLabReportRequest request) {



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



        LabReport report =
                new LabReport();



        report.setLabOrder(labOrder);


        report.setReport(
                request.getReport()
        );


        report.setFilePath(
                uploadDir + fileName
        );


        report.setStatus(
                LabReportStatus.UPLOADED
        );


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

}
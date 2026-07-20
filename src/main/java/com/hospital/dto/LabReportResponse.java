package com.hospital.dto;


import com.hospital.enums.LabReportStatus;

import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;


@Data
@Builder
public class LabReportResponse {


    private Long id;


    private Long labOrderId;


    private String report;


    private String filePath;


    private LabReportStatus status;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

}
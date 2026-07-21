package com.hospital.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadLabReportRequest {

    @NotNull(message = "Lab Order ID is required")
    @Positive(message = "Lab Order ID must be greater than 0")
    private Long labOrderId;

    @NotNull(message = "Report file is required")
    private MultipartFile file;

    @Size(max = 5000,
            message = "Report remarks cannot exceed 5000 characters")
    private String report;

}
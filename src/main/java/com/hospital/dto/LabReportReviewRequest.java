package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LabReportReviewRequest {

    @NotBlank(message = "Doctor remarks are required")
    @Size(
            min = 5,
            max = 1000,
            message = "Doctor remarks must be between 5 and 1000 characters"
    )
    private String doctorRemarks;

}
package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LabReportReviewRequest {


    @NotBlank
    private String doctorRemarks;

}
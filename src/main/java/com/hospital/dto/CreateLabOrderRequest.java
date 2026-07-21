package com.hospital.dto;

import com.hospital.enums.LabPriority;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateLabOrderRequest {

    @NotNull(message = "Appointment ID is required")
    @Positive(message = "Appointment ID must be greater than 0")
    private Long appointmentId;

    @NotNull(message = "Lab Test ID is required")
    @Positive(message = "Lab Test ID must be greater than 0")
    private Long labTestId;

    @NotBlank(message = "Clinical notes are required")
    @Size(
            min = 5,
            max = 1000,
            message = "Clinical notes must be between 5 and 1000 characters"
    )
    private String clinicalNotes;

    @NotNull(message = "Priority is required")
    private LabPriority priority;

    @Size(
            max = 1000,
            message = "Instructions cannot exceed 1000 characters"
    )
    private String instructions;
}
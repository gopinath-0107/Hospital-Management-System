package com.hospital.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateConsultationRequest {

    @NotNull(message = "Appointment Id is required")
    @Positive(message = "Appointment Id must be greater than 0")
    private Long appointmentId;


    @NotBlank(message = "Blood pressure is required")
    @Pattern(
            regexp = "^\\d{2,3}/\\d{2,3}$",
            message = "Blood pressure must be in format 120/80"
    )
    private String bloodPressure;

    @NotNull(message = "Temperature is required")
    @DecimalMin(
            value = "90.0",
            message = "Temperature is too low"
    )
    @DecimalMax(
            value = "110.0",
            message = "Temperature is too high"
    )
    private Double temperature;

    @NotNull(message = "Pulse rate is required")
    @Min(value = 30, message = "Pulse rate is too low")
    @Max(value = 220, message = "Pulse rate is too high")
    private Integer pulseRate;

    @NotBlank(message = "Diagnosis is required")
    @Size(
            min = 5,
            max = 1000,
            message = "Diagnosis must be between 5 and 1000 characters"
    )
    private String diagnosis;

    @Size(
            max = 2000,
            message = "Notes cannot exceed 2000 characters"
    )
    private String notes;

}
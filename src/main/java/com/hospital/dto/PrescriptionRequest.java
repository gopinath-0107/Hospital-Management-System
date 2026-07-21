package com.hospital.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequest {

    @NotNull(message = "Appointment ID is required")
    @Positive(message = "Appointment ID must be greater than 0")
    private Long appointmentId;

    @NotBlank(message = "Diagnosis is required")
    @Size(min = 5, max = 1000,
            message = "Diagnosis must be between 5 and 1000 characters")
    private String diagnosis;

    @NotEmpty(message = "At least one medicine is required")
    @Valid
    private List<PrescriptionMedicineRequest> medicines;

    @Size(max = 1000,
            message = "Instructions cannot exceed 1000 characters")
    private String instructions;
}
package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequest {


    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;


    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;


    @NotEmpty(message = "Medicine list required")
    private List<PrescriptionMedicineRequest> medicines;


    private String instructions;
}
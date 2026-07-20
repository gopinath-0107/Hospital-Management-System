package com.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponse {

    private Long prescriptionId;

    private Long appointmentId;

    private String patientName;

    private String doctorName;

    private String diagnosis;

    private List<com.hospital.dto.PrescriptionMedicineResponse> medicines;

    private String instructions;

    private String status;

    private LocalDateTime createdAt;

}
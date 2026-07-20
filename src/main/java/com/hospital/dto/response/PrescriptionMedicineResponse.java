package com.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionMedicineResponse {

    private Long medicineId;

    private String medicineName;

    private String dosage;

    private String frequency;

    private String duration;

    private Integer quantity;

}
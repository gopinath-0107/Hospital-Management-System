package com.hospital.dto;

import lombok.Data;


@Data
public class PrescriptionMedicineRequest {


    private Long medicineId;

    private String dosage;

    private Integer quantity;

    private String frequency;

    private String  duration;

}
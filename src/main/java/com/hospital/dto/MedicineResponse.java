package com.hospital.dto;

import com.hospital.enums.MedicineCategory;
import com.hospital.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class MedicineResponse {

    private Long id;

    private String medicineCode;

    private String medicineName;

    private String company;

    private MedicineCategory category;

    private String description;

    private Integer stockQuantity;

    private BigDecimal price;

    private String batchNumber;

    private LocalDate manufacturingDate;

    private LocalDate expiryDate;

    private Status status;

}
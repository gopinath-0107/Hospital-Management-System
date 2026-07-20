package com.hospital.dto;

import com.hospital.enums.MedicineCategory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class MedicineRequest {

    private String medicineName;

    private String company;

    private MedicineCategory category;

    private String description;

    private Integer stockQuantity;

    private BigDecimal price;

    private String batchNumber;

    private LocalDate manufacturingDate;

    private LocalDate expiryDate;

}
package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PrescriptionMedicineRequest {

    @NotNull(message = "Medicine ID is required")
    @Positive(message = "Medicine ID must be greater than 0")
    private Long medicineId;


    @NotBlank(message = "Dosage is required")
    @Size(
            min = 2,
            max = 50,
            message = "Dosage must be between 2 and 50 characters"
    )
    private String dosage;


    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;


    @NotBlank(message = "Frequency is required")
    @Size(
            min = 2,
            max = 50,
            message = "Frequency must be between 2 and 50 characters"
    )
    private String frequency;


    @NotBlank(message = "Duration is required")
    @Size(
            min = 1,
            max = 30,
            message = "Duration must be between 1 and 30 characters"
    )
    private String duration;

}
package com.hospital.dto;

import com.hospital.enums.MedicineCategory;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class MedicineRequest {

    @NotBlank(message = "Medicine name is required")
    @Size(min = 2, max = 100,
            message = "Medicine name must be between 2 and 100 characters")
    private String medicineName;


    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100,
            message = "Company name must be between 2 and 100 characters")
    private String company;


    @NotNull(message = "Medicine category is required")
    private MedicineCategory category;


    @Size(max = 500,
            message = "Description cannot exceed 500 characters")
    private String description;


    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;


    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01",
            message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2,
            message = "Invalid price")
    private BigDecimal price;


    @NotBlank(message = "Batch number is required")
    @Size(min = 3, max = 50,
            message = "Batch number must be between 3 and 50 characters")
    private String batchNumber;


    @NotNull(message = "Manufacturing date is required")
    @PastOrPresent(message = "Manufacturing date cannot be in the future")
    private LocalDate manufacturingDate;


    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

}
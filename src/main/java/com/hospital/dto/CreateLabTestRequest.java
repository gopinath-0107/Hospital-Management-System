package com.hospital.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateLabTestRequest {

    @NotBlank(message = "Test name is required")
    @Size(
            min = 3,
            max = 100,
            message = "Test name must be between 3 and 100 characters"
    )
    @Pattern(
            regexp = "^[A-Za-z0-9()\\-.,/ ]+$",
            message = "Test name contains invalid characters"
    )
    private String testName;

    @Size(
            max = 500,
            message = "Description cannot exceed 500 characters"
    )
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(
            value = "0.01",
            inclusive = true,
            message = "Price must be greater than 0"
    )
    @Digits(
            integer = 8,
            fraction = 2,
            message = "Invalid price format"
    )
    private BigDecimal price;

}
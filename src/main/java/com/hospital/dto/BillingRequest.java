package com.hospital.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingRequest {

    @NotNull(message = "Appointment Id is required")
    @Positive(message = "Appointment Id must be greater than 0")
    private Long appointmentId;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private BigDecimal discount;
}
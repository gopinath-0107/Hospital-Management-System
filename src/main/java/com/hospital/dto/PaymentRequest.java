package com.hospital.dto;

import com.hospital.enums.PaymentMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "Payment amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Payment amount must be greater than 0"
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Invalid payment amount"
    )
    private BigDecimal amount;


    @NotNull(message = "Payment mode is required")
    private PaymentMode paymentMode;

}
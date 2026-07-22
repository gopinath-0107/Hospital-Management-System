package com.hospital.dto;

import com.hospital.enums.PaymentMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LaboratoryPaymentRequest {

    @NotNull(message = "Lab Order Id is required")
    private Long labOrderId;

    @NotNull(message = "Payment method is required")
    private PaymentMode paymentMode;

}
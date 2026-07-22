package com.hospital.dto;

import com.hospital.enums.PaymentMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PharmacyPaymentRequest {

    @NotNull(message = "Prescription Id is required")
    private Long prescriptionId;

    @NotNull(message = "Payment method is required")
    private PaymentMode paymentMode;

}
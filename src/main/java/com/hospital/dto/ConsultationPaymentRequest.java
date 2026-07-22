package com.hospital.dto;

import com.hospital.enums.PaymentMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsultationPaymentRequest {

    @NotNull(message = "Appointment Id is required")
    private Long appointmentId;

    @NotNull(message = "Payment method is required")
    private PaymentMode paymentMode;

}
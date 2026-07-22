package com.hospital.service;

import com.hospital.dto.PaymentRequest;
import com.hospital.dto.PaymentResponse;
import com.hospital.dto.ReceiptResponse;

public interface PaymentService {

    PaymentResponse consultationPayment(
            Long appointmentId,
            PaymentRequest request
    );

    PaymentResponse pharmacyPayment(
            Long prescriptionId,
            PaymentRequest request
    );

    PaymentResponse laboratoryPayment(
            Long labOrderId,
            PaymentRequest request
    );

    ReceiptResponse getConsultationReceipt(Long appointmentId);

    ReceiptResponse getPharmacyReceipt(Long prescriptionId);

    ReceiptResponse getLaboratoryReceipt(Long labOrderId);
}

package com.hospital.serviceImpl;

import com.hospital.dto.PaymentRequest;
import com.hospital.dto.PaymentResponse;
import com.hospital.dto.ReceiptResponse;
import com.hospital.entity.*;
import com.hospital.enums.PaymentStatus;
import com.hospital.enums.PaymentType;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.AppointmentRepository;
import com.hospital.repo.LabOrderRepository;
import com.hospital.repo.PaymentRepository;
import com.hospital.repo.PrescriptionRepository;
import com.hospital.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final LabOrderRepository labOrderRepository;

    @Override
    public PaymentResponse consultationPayment(
            Long appointmentId,
            PaymentRequest request) {

        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Appointment not found"));

        paymentRepository.findByAppointmentId(appointmentId)
                .ifPresent(payment -> {
                    throw new IllegalArgumentException(
                            "Consultation payment already completed.");
                });

        Doctor doctor = appointment.getDoctor();

        Payment payment = Payment.builder()
                .appointment(appointment)
                .amount(doctor.getConsultationFee())
                .paymentMode(request.getPaymentMode())
                .paymentStatus(PaymentStatus.PAID)
                .paymentType(PaymentType.CONSULTATION)
                .transactionId(generateTransactionId())
                .receiptNumber(generateReceiptNumber())
                .remarks("Consultation fee paid successfully")
                .paymentDate(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return mapToPaymentResponse(savedPayment);
    }

    // ======================================================
    // Helper Methods
    // ======================================================

    private PaymentResponse mapToPaymentResponse(Payment payment) {

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .paymentMode(payment.getPaymentMode())
                .paymentStatus(payment.getPaymentStatus())
                .paymentType(payment.getPaymentType())
                .transactionId(payment.getTransactionId())
                .receiptNumber(payment.getReceiptNumber())
                .paymentDate(payment.getPaymentDate())
                .build();
    }

    private ReceiptResponse mapToReceiptResponse(Payment payment) {

        return ReceiptResponse.builder()
                .receiptNumber(payment.getReceiptNumber())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .paymentMode(payment.getPaymentMode())
                .paymentStatus(payment.getPaymentStatus())
                .paymentType(payment.getPaymentType())
                .paymentDate(payment.getPaymentDate())
                .build();
    }

    private String generateReceiptNumber() {

        return "RCPT-" + System.currentTimeMillis();
    }

    private String generateTransactionId() {

        return UUID.randomUUID().toString();
    }


    @Override
    public PaymentResponse pharmacyPayment(
            Long prescriptionId,
            PaymentRequest request) {

        Prescription prescription =
                prescriptionRepository.findById(prescriptionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Prescription not found"));

        paymentRepository.findByPrescriptionId(prescriptionId)
                .ifPresent(payment -> {
                    throw new IllegalArgumentException(
                            "Medicine payment already completed.");
                });

        BigDecimal totalAmount = calculateMedicineAmount(prescription);

        Payment payment = Payment.builder()
                .prescription(prescription)
                .amount(totalAmount)
                .paymentMode(request.getPaymentMode())
                .paymentStatus(PaymentStatus.PAID)
                .paymentType(PaymentType.PHARMACY)
                .transactionId(generateTransactionId())
                .receiptNumber(generateReceiptNumber())
                .remarks("Medicine payment successful")
                .paymentDate(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return mapToPaymentResponse(savedPayment);
    }


    private BigDecimal calculateMedicineAmount(
            Prescription prescription) {

        BigDecimal total = BigDecimal.ZERO;

        for (PrescriptionMedicine prescriptionMedicine
                : prescription.getMedicines()) {

            BigDecimal medicinePrice =
                    prescriptionMedicine.getMedicine().getPrice();

            BigDecimal quantity =
                    BigDecimal.valueOf(
                            prescriptionMedicine.getQuantity());

            total = total.add(
                    medicinePrice.multiply(quantity)
            );
        }

        return total;
    }

    @Override
    public PaymentResponse laboratoryPayment(
            Long labOrderId,
            PaymentRequest request) {

        LabOrder labOrder = labOrderRepository.findById(labOrderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lab Order not found"));

        paymentRepository.findByLabOrderId(labOrderId)
                .ifPresent(payment -> {
                    throw new IllegalArgumentException(
                            "Laboratory payment already completed.");
                });

        BigDecimal amount = labOrder.getLabTest().getPrice();

        Payment payment = Payment.builder()
                .labOrder(labOrder)
                .amount(amount)
                .paymentMode(request.getPaymentMode())
                .paymentStatus(PaymentStatus.PAID)
                .paymentType(PaymentType.LABORATORY)
                .transactionId(generateTransactionId())
                .receiptNumber(generateReceiptNumber())
                .remarks("Laboratory payment successful")
                .paymentDate(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return mapToPaymentResponse(savedPayment);
    }


    @Override
    public ReceiptResponse getConsultationReceipt(Long appointmentId) {

        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Consultation receipt not found"));

        return mapToReceiptResponse(payment);
    }


    @Override
    public ReceiptResponse getPharmacyReceipt(Long prescriptionId) {

        Payment payment = paymentRepository.findByPrescriptionId(prescriptionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pharmacy receipt not found"));

        return mapToReceiptResponse(payment);
    }


    @Override
    public ReceiptResponse getLaboratoryReceipt(Long labOrderId) {

        Payment payment = paymentRepository.findByLabOrderId(labOrderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Laboratory receipt not found"));

        return mapToReceiptResponse(payment);
    }

}
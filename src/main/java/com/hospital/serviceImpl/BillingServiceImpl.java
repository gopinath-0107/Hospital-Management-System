package com.hospital.serviceImpl;

import com.hospital.dto.*;
import com.hospital.entity.*;
import com.hospital.enums.*;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.*;
import com.hospital.service.BillingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.hospital.dto.ReceiptResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class BillingServiceImpl implements BillingService {


    private final BillingRepository billingRepository;
    private final PaymentRepository paymentRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final LabOrderRepository labOrderRepository;


    @Override
    public BillingResponse generateBill(BillingRequest request) {


        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found"));

        Prescription prescription =
                prescriptionRepository
                        .findByAppointmentId(
                                appointment.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prescription not found"
                                ));


        if (!prescription.getMedicines().isEmpty()) {

            if (prescription.getStatus() != PrescriptionStatus.DISPENSED) {

                throw new RuntimeException(
                        "Medicines not dispensed"
                );
            }

        }


        Optional<LabOrder> labOrder =
                labOrderRepository
                        .findFirstByAppointmentId(
                                appointment.getId()
                        );

        if (labOrder.isPresent()) {

            if (labOrder.get().getStatus() != LabOrderStatus.COMPLETED) {

                throw new RuntimeException(
                        "Lab report pending"
                );
            }

        }

        if(billingRepository.existsByAppointmentId(
                request.getAppointmentId())){

            throw new RuntimeException(
                    "Bill already generated"
            );
        }

        if (appointment.getStatus() != AppointmentStatus.APPROVED) {
            throw new RuntimeException(
                    "Billing can only be generated for approved appointments."
            );
        }

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));


                prescriptionRepository.findByAppointmentId(
                        appointment.getId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException("Prescription not found"));

        if (prescription.getStatus() != PrescriptionStatus.DISPENSED) {
            throw new RuntimeException(
                    "Medicine must be dispensed before billing."
            );
        }





        BigDecimal consultationFee =
                request.getConsultationFee() != null ?
                        request.getConsultationFee() :
                        BigDecimal.ZERO;


        BigDecimal medicineAmount =
                request.getMedicineAmount() != null ?
                        request.getMedicineAmount() :
                        BigDecimal.ZERO;


        BigDecimal labAmount =
                request.getLabAmount() != null ?
                        request.getLabAmount() :
                        BigDecimal.ZERO;


        BigDecimal discount =
                request.getDiscount() != null ?
                        request.getDiscount() :
                        BigDecimal.ZERO;



        BigDecimal totalAmount =
                consultationFee
                        .add(medicineAmount)
                        .add(labAmount)
                        .subtract(discount);



        Billing billing = Billing.builder()
                .patient(patient)
                .appointment(appointment)
                .consultationFee(consultationFee)
                .medicineAmount(medicineAmount)
                .labAmount(labAmount)
                .discount(discount)
                .totalAmount(totalAmount)
                .billingStatus(BillingStatus.UNPAID)
                .build();



        Billing savedBilling = billingRepository.save(billing);


        return mapToResponse(savedBilling);

    }





    @Override
    public BillingResponse getBillById(Long billingId) {


        Billing billing = billingRepository.findById(billingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bill not found"));


        return mapToResponse(billing);

    }





    @Override
    public List<BillingResponse> getAllBills() {


        return billingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }





    @Override
    public PaymentResponse makePayment(Long billingId, PaymentRequest request) {


        if(paymentRepository.existsByBillingId(billingId)){

            throw new RuntimeException(
                    "Payment already completed"
            );
        }

        Billing billing = billingRepository.findById(billingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bill not found"));


        if (billing.getBillingStatus() == BillingStatus.PAID) {
            throw new RuntimeException("Bill is already paid.");
        }


        Payment payment = Payment.builder()
                .billing(billing)
                .amount(request.getAmount())
                .paymentMode(request.getPaymentMode())
                .paymentStatus(PaymentStatus.PAID)
                .build();


        if (request.getAmount() == null) {
            throw new RuntimeException("Payment amount is required.");
        }

        if (request.getAmount().compareTo(billing.getTotalAmount()) != 0) {
            throw new RuntimeException("Invalid payment amount.");
        }

        Payment savedPayment = paymentRepository.save(payment);



        billing.setBillingStatus(BillingStatus.PAID);

        billingRepository.save(billing);

        Appointment appointment = billing.getAppointment();

        appointment.setStatus(AppointmentStatus.COMPLETED);

        appointmentRepository.save(appointment);


        return PaymentResponse.builder()
                .paymentId(savedPayment.getId())
                .billingId(billing.getId())
                .amount(savedPayment.getAmount())
                .paymentMode(savedPayment.getPaymentMode())
                .paymentStatus(savedPayment.getPaymentStatus())
                .paymentDate(savedPayment.getPaymentDate())
                .build();

    }





    private BillingResponse mapToResponse(Billing billing) {


        return BillingResponse.builder()

                .billingId(billing.getId())

                .patientId(billing.getPatient().getId())

                .appointmentId(billing.getAppointment().getId())

                .consultationFee(billing.getConsultationFee())

                .medicineAmount(billing.getMedicineAmount())

                .labAmount(billing.getLabAmount())

                .discount(billing.getDiscount())

                .totalAmount(billing.getTotalAmount())

                .billingStatus(billing.getBillingStatus())

                .createdAt(billing.getCreatedAt())

                .build();

    }


    @Override
    public ReceiptResponse getReceipt(Long billingId) {

        Billing billing = billingRepository.findById(billingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bill not found"));

        List<Payment> payments =
                paymentRepository.findByBillingId(billingId);

        if (payments.isEmpty()) {
            throw new ResourceNotFoundException("Payment not found");
        }

        Payment payment = payments.get(payments.size() - 1);

        return ReceiptResponse.builder()
                .billingId(billing.getId())
                .patientId(billing.getPatient().getId())
                .appointmentId(billing.getAppointment().getId())
                .consultationFee(billing.getConsultationFee())
                .medicineAmount(billing.getMedicineAmount())
                .labAmount(billing.getLabAmount())
                .discount(billing.getDiscount())
                .totalAmount(billing.getTotalAmount())
                .paymentMode(payment.getPaymentMode().name())
                .paymentStatus(payment.getPaymentStatus().name())
                .paymentDate(payment.getPaymentDate())
                .build();
    }

}
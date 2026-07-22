package com.hospital.repo;

import com.hospital.entity.Payment;
import com.hospital.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByPaymentType(PaymentType paymentType);

    Optional<Payment> findByAppointmentId(Long appointmentId);

    Optional<Payment> findByPrescriptionId(Long prescriptionId);

    Optional<Payment> findByLabOrderId(Long labOrderId);

}
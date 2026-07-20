package com.hospital.repo;

import com.hospital.entity.Payment;
import com.hospital.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByBillingId(Long billingId);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    boolean existsByBillingId(Long billingId);

}
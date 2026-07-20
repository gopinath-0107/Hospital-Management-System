package com.hospital.repo;

import com.hospital.entity.Billing;
import com.hospital.enums.BillingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillingRepository extends JpaRepository<Billing, Long> {

    List<Billing> findByPatientId(Long patientId);

    List<Billing> findByBillingStatus(BillingStatus billingStatus);

    Optional<Billing> findByAppointmentId(Long appointmentId);

    boolean existsByAppointmentId(Long appointmentId);
}
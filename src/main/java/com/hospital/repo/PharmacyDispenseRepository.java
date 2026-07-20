package com.hospital.repo;

import com.hospital.entity.PharmacyDispense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PharmacyDispenseRepository extends JpaRepository<PharmacyDispense, Long> {

    List<PharmacyDispense> findByPharmacistId(Long pharmacistId);
    long count();

    Optional<PharmacyDispense> findByPrescriptionId(Long prescriptionId);
}
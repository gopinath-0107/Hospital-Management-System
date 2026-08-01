package com.hospital.repo;

import com.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    Optional<Patient> findByEmailOrMobile(
            String email,
            String mobile
    );

    Optional<Patient> findByEmail(String email);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM Patient p WHERE p.firstName LIKE %:query% OR p.lastName LIKE %:query% OR p.mobile LIKE %:query%")
    java.util.List<Patient> searchPatients(@org.springframework.data.repository.query.Param("query") String query);
}

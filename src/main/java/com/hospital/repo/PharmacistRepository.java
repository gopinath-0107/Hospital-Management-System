package com.hospital.repo;

import com.hospital.entity.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {

    Optional<Pharmacist> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    boolean existsByLicenseNumber(String licenseNumber);

}
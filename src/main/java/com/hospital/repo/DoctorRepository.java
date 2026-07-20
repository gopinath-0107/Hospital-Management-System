package com.hospital.repo;

import com.hospital.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    boolean existsByLicenseNumber(String licenseNumber);

    Optional<Doctor> findByEmail(String email);

    java.util.List<Doctor> findByDepartmentIdAndAvailableAndStatus(Long departmentId, boolean available, com.hospital.enums.Status status);
}

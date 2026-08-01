package com.hospital.repo;

import com.hospital.entity.Doctor;
import com.hospital.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    boolean existsByLicenseNumber(String licenseNumber);

    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByEmailOrMobile(String email, String mobile);

    List<Doctor> findByDepartmentIdAndSpecializationIdAndAvailableAndStatus(
            Long departmentId,
            Long specializationId,
            Boolean available,
            Status status
    );
}
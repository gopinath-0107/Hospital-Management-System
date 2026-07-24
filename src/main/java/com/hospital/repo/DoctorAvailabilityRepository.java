package com.hospital.repo;

import com.hospital.entity.DoctorAvailability;
import com.hospital.enums.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

    Optional<DoctorAvailability> findByDoctorIdAndAvailableDate(
            Long doctorId,
            LocalDate availableDate
    );

    List<DoctorAvailability> findByDoctorId(Long doctorId);

    List<DoctorAvailability> findByAvailableDate(LocalDate availableDate);

    List<DoctorAvailability> findByStatus(
            AvailabilityStatus status
    );

    boolean existsByDoctorIdAndAvailableDate(
            Long doctorId,
            LocalDate availableDate
    );

}
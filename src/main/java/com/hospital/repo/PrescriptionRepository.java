package com.hospital.repo;

import com.hospital.entity.Prescription;
import com.hospital.enums.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {


    Optional<Prescription> findByAppointmentId(Long appointmentId);

    List<Prescription> findByStatus(PrescriptionStatus status);

    long countByAppointmentDoctorId(Long doctorId);

    long countByAppointmentPatientId(Long patientId);
}
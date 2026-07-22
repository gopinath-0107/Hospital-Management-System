package com.hospital.repo;

import com.hospital.entity.Appointment;
import com.hospital.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    long countByDoctorId(Long doctorId);

    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    long countByStatus(AppointmentStatus status);

    boolean existsByDoctorIdAndAppointmentDate(
            Long doctorId,
            LocalDateTime appointmentDate
    );

    List<Appointment> findByDoctorIdAndAppointmentDateBetween(
            Long doctorId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    long countByPatientId(Long patientId);

    long countByPatientIdAndStatus(
            Long patientId,
            AppointmentStatus status
    );


}

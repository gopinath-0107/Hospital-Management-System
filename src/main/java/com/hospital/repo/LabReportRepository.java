package com.hospital.repo;

import com.hospital.entity.LabReport;
import com.hospital.enums.LabReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabReportRepository extends JpaRepository<LabReport, Long> {

    Optional<LabReport> findByLabOrderId(Long labOrderId);

    boolean existsByLabOrderId(Long labOrderId);

    long countByLabOrderAppointmentPatientId(Long patientId);
    long countByStatus(LabReportStatus status);

    List<LabReport> findByLabOrderAppointmentDoctorId(Long doctorId);

    List<LabReport> findByLabOrderAppointmentPatientId(Long patientId);

    List<LabReport> findByLabTechnicianId(Long labTechnicianId);
}
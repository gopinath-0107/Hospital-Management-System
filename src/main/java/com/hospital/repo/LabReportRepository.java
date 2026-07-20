package com.hospital.repo;

import com.hospital.entity.LabReport;
import com.hospital.enums.LabReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabReportRepository extends JpaRepository<LabReport, Long> {

    Optional<LabReport> findByLabOrderId(Long labOrderId);

    boolean existsByLabOrderId(Long labOrderId);

    long countByStatus(LabReportStatus status);
}
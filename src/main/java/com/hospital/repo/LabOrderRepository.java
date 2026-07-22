package com.hospital.repo;

import com.hospital.entity.LabOrder;
import com.hospital.enums.LabOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {

    List<LabOrder> findByAppointmentId(Long appointmentId);

    List<LabOrder> findByStatus(LabOrderStatus status);

    List<LabOrder> findByAppointmentDoctorId(Long doctorId);

    List<LabOrder> findByAppointmentPatientId(Long patientId);


    long countByAppointmentPatientId(Long patientId);

    Optional<LabOrder> findFirstByAppointmentId(Long appointmentId);

    long countByStatus(LabOrderStatus status);
    boolean existsByAppointmentIdAndLabTestId(
            Long appointmentId,
            Long labTestId
    );



}
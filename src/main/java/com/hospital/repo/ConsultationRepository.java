package com.hospital.repo;


import com.hospital.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ConsultationRepository
        extends JpaRepository<Consultation,Long>{

    boolean existsByAppointmentId(Long appointmentId);

    Consultation findByAppointmentId(Long appointmentId);

    long countByDoctorId(Long doctorId);

}
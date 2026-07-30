package com.hospital.repo;

import com.hospital.entity.VoiceConsultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VoiceConsultationRepository extends JpaRepository<VoiceConsultation, Long> {
    Optional<VoiceConsultation> findByAppointmentId(Long appointmentId);
}

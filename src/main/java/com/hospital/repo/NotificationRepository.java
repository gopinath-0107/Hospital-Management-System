package com.hospital.repo;

import com.hospital.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    long countByPatientIdAndIsReadFalse(Long patientId);

}
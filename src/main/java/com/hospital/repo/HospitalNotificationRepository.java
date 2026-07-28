package com.hospital.repo;

import com.hospital.entity.HospitalNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HospitalNotificationRepository
        extends JpaRepository<HospitalNotification,Long> {

    List<HospitalNotification>
    findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndIsReadFalse(Long userId);
}
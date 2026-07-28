package com.hospital.repo;

import com.hospital.entity.HospitalNotification;
import com.hospital.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HospitalNotificationRepository
        extends JpaRepository<HospitalNotification,Long> {

    List<HospitalNotification> findByUserIdAndRoleOrderByCreatedAtDesc(
            Long userId,
            Role role
    );


    long countByUserIdAndRoleAndIsReadFalse(
            Long userId,
            Role role
    );
}
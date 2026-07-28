package com.hospital.service;

import com.hospital.dto.HospitalNotificationResponse;
import com.hospital.enums.NotificationType;
import com.hospital.enums.Role;

import java.util.List;

public interface HospitalNotificationService {

    void createNotification(
            Long userId,
            Role role,
            NotificationType type,
            String title,
            String message
    );

    List<HospitalNotificationResponse> getMyNotifications();

    void markAsRead(Long id);

    void markAllAsRead();

    void deleteNotification(Long id);

    long getUnreadCount();
}
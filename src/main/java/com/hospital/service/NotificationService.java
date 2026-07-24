package com.hospital.service;

import com.hospital.dto.NotificationRequest;
import com.hospital.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);

    List<NotificationResponse> getPatientNotifications(Long patientId);

    NotificationResponse markAsRead(Long notificationId);

    void markAllAsRead(Long patientId);

}
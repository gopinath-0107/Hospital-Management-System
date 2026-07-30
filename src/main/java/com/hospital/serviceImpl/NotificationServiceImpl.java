package com.hospital.serviceImpl;

import com.hospital.dto.NotificationRequest;
import com.hospital.dto.NotificationResponse;
import com.hospital.entity.Appointment;
import com.hospital.entity.Notification;
import com.hospital.entity.Patient;
import com.hospital.enums.AppointmentStatus;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.AppointmentRepository;
import com.hospital.repo.NotificationRepository;
import com.hospital.repo.PatientRepository;
import com.hospital.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public NotificationResponse createNotification(NotificationRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));

        Notification notification = Notification.builder()
                .patient(patient)
                .title(request.getTitle())
                .message(request.getMessage())
                .build();

        if (request.isEmergency()) {

            Appointment appointment = appointmentRepository
                    .findById(request.getAppointmentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Appointment not found"));

            appointment.setStatus(AppointmentStatus.CANCELLED);

            appointmentRepository.save(appointment);
        }

        return map(notificationRepository.save(notification));
    }

    @Override
    public List<NotificationResponse> getPatientNotifications(Long patientId) {

        return notificationRepository
                .findByPatientIdOrderByCreatedAtDesc(patientId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public NotificationResponse markAsRead(Long notificationId) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Notification not found"));

        notification.setIsRead(true);

        return map(notificationRepository.save(notification));
    }

    @Override
    public void markAllAsRead(Long patientId) {

        List<Notification> notifications =
                notificationRepository.findByPatientIdOrderByCreatedAtDesc(patientId);

        for (Notification notification : notifications) {

            notification.setIsRead(true);
        }

        notificationRepository.saveAll(notifications);
    }

    private NotificationResponse map(Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .patientId(notification.getPatient().getId())
                .patientName(
                        notification.getPatient().getFirstName() + " "
                                + notification.getPatient().getLastName()
                )
                .title(notification.getTitle())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
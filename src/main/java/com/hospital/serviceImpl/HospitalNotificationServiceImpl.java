package com.hospital.serviceImpl;

import com.hospital.dto.HospitalNotificationResponse;
import com.hospital.entity.*;
import com.hospital.enums.NotificationType;
import com.hospital.enums.Role;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.*;
import com.hospital.service.HospitalNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalNotificationServiceImpl
        implements HospitalNotificationService {

    private final HospitalNotificationRepository hospitalNotificationRepository;

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    private final NurseRepository nurseRepository;
    private final PharmacistRepository pharmacistRepository;
    private final ReceptionistRepository receptionistRepository;
    private final LabTechnicianRepository labTechnicianRepository;

    @Override
    @Transactional
    public void createNotification(
            Long userId,
            Role role,
            NotificationType type,
            String title,
            String message) {

        HospitalNotification notification =
                HospitalNotification.builder()
                        .userId(userId)
                        .role(role)
                        .type(type)
                        .title(title)
                        .message(message)
                        .isRead(false)
                        .createdAt(LocalDateTime.now())
                        .build();

        hospitalNotificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HospitalNotificationResponse> getMyNotifications() {

        Long userId = getLoggedInUserId();

        List<HospitalNotification> notifications =
                hospitalNotificationRepository
                        .findByUserIdOrderByCreatedAtDesc(userId);

        List<HospitalNotificationResponse> response =
                new ArrayList<>();

        for (HospitalNotification notification : notifications) {

            response.add(
                    HospitalNotificationResponse.builder()
                            .id(notification.getId())
                            .title(notification.getTitle())
                            .message(notification.getMessage())
                            .type(notification.getType())
                            .isRead(notification.getIsRead())
                            .createdAt(notification.getCreatedAt())
                            .build()
            );
        }

        return response;
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {

        HospitalNotification notification =
                hospitalNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"));

        notification.setIsRead(true);

        hospitalNotificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead() {

        Long userId = getLoggedInUserId();

        List<HospitalNotification> notifications =
                hospitalNotificationRepository
                        .findByUserIdOrderByCreatedAtDesc(userId);

        for (HospitalNotification notification : notifications) {
            notification.setIsRead(true);
        }

        hospitalNotificationRepository.saveAll(notifications);
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {

        HospitalNotification notification =
                hospitalNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"));

        hospitalNotificationRepository.delete(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount() {

        Long userId = getLoggedInUserId();

        return hospitalNotificationRepository
                .countByUserIdAndIsReadFalse(userId);
    }

    private Long getLoggedInUserId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return adminRepository.findByEmail(email)
                .map(Admin::getId)

                .or(() -> doctorRepository.findByEmail(email)
                        .map(Doctor::getId))

                .or(() -> patientRepository.findByEmail(email)
                        .map(Patient::getId))



                .or(() -> nurseRepository.findByEmail(email)
                        .map(Nurse::getId))

                .or(() -> pharmacistRepository.findByEmail(email)
                        .map(Pharmacist::getId))

                .or(() -> receptionistRepository.findByEmail(email)
                        .map(Receptionist::getId))

                .or(() -> labTechnicianRepository.findByEmail(email)
                        .map(LabTechnician::getId))

                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Logged in user not found"));
    }

    private Role getLoggedInRole() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        if (adminRepository.existsByEmail(email))
            return Role.ADMIN;

        if (doctorRepository.existsByEmail(email))
            return Role.DOCTOR;

        if (patientRepository.existsByEmail(email))
            return Role.PATIENT;



        if (nurseRepository.existsByEmail(email))
            return Role.NURSE;

        if (pharmacistRepository.existsByEmail(email))
            return Role.PHARMACIST;

        if (receptionistRepository.existsByEmail(email))
            return Role.RECEPTIONIST;

        if (labTechnicianRepository.existsByEmail(email))
            return Role.LAB_TECHNICIAN;

        throw new ResourceNotFoundException("Role not found");
    }
}
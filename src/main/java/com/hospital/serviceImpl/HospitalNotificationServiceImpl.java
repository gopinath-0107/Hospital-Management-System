package com.hospital.serviceImpl;

import com.hospital.dto.HospitalNotificationResponse;
import com.hospital.entity.HospitalNotification;
import com.hospital.enums.NotificationType;
import com.hospital.enums.Role;
import com.hospital.repo.HospitalNotificationRepository;
import com.hospital.security.CustomUserDetails;
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
@Transactional
public class HospitalNotificationServiceImpl
        implements HospitalNotificationService {

    private final HospitalNotificationRepository hospitalNotificationRepository;

    // =====================================================
    // CREATE NOTIFICATION
    // =====================================================

    @Override
    public void createNotification(
            Long userId,
            Role role,
            NotificationType type,
            String title,
            String message
    ) {

        HospitalNotification notification = new HospitalNotification();

        notification.setUserId(userId);
        notification.setRole(role);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        hospitalNotificationRepository.save(notification);
    }

    // =====================================================
    // GET LOGGED USER NOTIFICATIONS
    // =====================================================

    @Override
    public List<HospitalNotificationResponse> getMyNotifications() {

        Long userId = getLoggedInUserId();

        Role role = getLoggedInRole();

        List<HospitalNotification> notifications =
                hospitalNotificationRepository
                        .findByUserIdAndRoleOrderByCreatedAtDesc(
                                userId,
                                role
                        );

        List<HospitalNotificationResponse> responseList =
                new ArrayList<>();

        for (HospitalNotification notification : notifications) {

            HospitalNotificationResponse response =
                    new HospitalNotificationResponse();

            response.setId(
                    notification.getId()
            );

            response.setTitle(
                    notification.getTitle()
            );

            response.setMessage(
                    notification.getMessage()
            );

            response.setType(
                    notification.getType()
            );

            response.setRole(
                    notification.getRole()
            );

            response.setRead(
                    notification.getIsRead()
            );

            response.setCreatedAt(
                    notification.getCreatedAt()
            );

            responseList.add(response);
        }

        return responseList;
    }

    // =====================================================
    // MARK SINGLE NOTIFICATION AS READ
    // =====================================================

    @Override
    public void markAsRead(Long id) {

        HospitalNotification notification =
                hospitalNotificationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Notification not found")
                        );

        notification.setIsRead(true);

        hospitalNotificationRepository.save(notification);
    }



    // =====================================================
    // MARK ALL NOTIFICATIONS AS READ
    // =====================================================

    @Override
    public void markAllAsRead() {

        Long userId = getLoggedInUserId();

        Role role = getLoggedInRole();

        List<HospitalNotification> notifications =
                hospitalNotificationRepository
                        .findByUserIdAndRoleOrderByCreatedAtDesc(
                                userId,
                                role
                        );

        for (HospitalNotification notification : notifications) {
            notification.setIsRead(true);
        }

        hospitalNotificationRepository.saveAll(notifications);
    }



    // =====================================================
    // DELETE NOTIFICATION
    // =====================================================

    @Override
    public void deleteNotification(Long id) {

        HospitalNotification notification =
                hospitalNotificationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Notification not found")
                        );

        hospitalNotificationRepository.delete(notification);
    }

    // =====================================================
    // GET UNREAD NOTIFICATION COUNT
    // =====================================================

    @Override
    public long getUnreadCount() {

        Long userId = getLoggedInUserId();

        Role role = getLoggedInRole();

        return hospitalNotificationRepository
                .countByUserIdAndRoleAndIsReadFalse(
                        userId,
                        role
                );
    }



    // =====================================================
    // GET LOGGED-IN USER ID
    // =====================================================

    private Long getLoggedInUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {

            CustomUserDetails userDetails =
                    (CustomUserDetails) principal;

            return userDetails.getId();
        }

        throw new RuntimeException("Logged user id not found");
    }



    // =====================================================
    // GET LOGGED-IN USER ROLE
    // =====================================================

    private Role getLoggedInRole() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {

            CustomUserDetails userDetails =
                    (CustomUserDetails) principal;

            return Role.valueOf(
                    userDetails.getRole()
            );
        }

        throw new RuntimeException("Logged user role not found");
    }

}
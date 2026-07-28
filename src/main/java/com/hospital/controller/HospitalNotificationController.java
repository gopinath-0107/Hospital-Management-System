package com.hospital.controller;

import com.hospital.dto.HospitalNotificationResponse;
import com.hospital.service.HospitalNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospital-notifications")
@RequiredArgsConstructor
public class HospitalNotificationController {

    private final HospitalNotificationService hospitalNotificationService;

    // Logged-in User Notifications
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT','RECEPTIONIST','LAB_TECHNICIAN','PHARMACIST')")
    public ResponseEntity<List<HospitalNotificationResponse>> getMyNotifications() {

        return ResponseEntity.ok(
                hospitalNotificationService.getMyNotifications()
        );
    }

    // Mark Notification As Read
    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT','RECEPTIONIST','LAB_TECHNICIAN','PHARMACIST')")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long id) {

        hospitalNotificationService.markAsRead(id);

        return ResponseEntity.ok(
                "Notification marked as read successfully."
        );
    }

    // Unread Notification Count
    @GetMapping("/unread-count")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT','RECEPTIONIST','LAB_TECHNICIAN','PHARMACIST')")
    public ResponseEntity<Long> getUnreadCount() {

        return ResponseEntity.ok(
                hospitalNotificationService.getUnreadCount()
        );
    }

    // Delete Notification
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT','RECEPTIONIST','LAB_TECHNICIAN','PHARMACIST')")
    public ResponseEntity<String> deleteNotification(
            @PathVariable Long id) {

        hospitalNotificationService.deleteNotification(id);

        return ResponseEntity.ok(
                "Notification deleted successfully."
        );
    }

    // Mark All Notifications As Read
    @PutMapping("/read-all")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT','RECEPTIONIST','LAB_TECHNICIAN','PHARMACIST')")
    public ResponseEntity<String> markAllAsRead() {

        hospitalNotificationService.markAllAsRead();

        return ResponseEntity.ok(
                "All notifications marked as read successfully."
        );
    }
}
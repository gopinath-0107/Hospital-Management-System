package com.hospital.controller;

import com.hospital.dto.NotificationRequest;
import com.hospital.dto.NotificationResponse;
import com.hospital.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('RECEPTIONIST')")
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody NotificationRequest request) {

        return ResponseEntity.ok(
                notificationService.createNotification(request)
        );
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<NotificationResponse>> getPatientNotifications(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                notificationService.getPatientNotifications(patientId)
        );
    }

    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable Long notificationId) {

        return ResponseEntity.ok(
                notificationService.markAsRead(notificationId)
        );
    }

    @PutMapping("/patient/{patientId}/read-all")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> markAllAsRead(
            @PathVariable Long patientId) {

        notificationService.markAllAsRead(patientId);

        return ResponseEntity.ok("All notifications marked as read.");
    }
}
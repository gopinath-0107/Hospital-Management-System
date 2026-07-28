package com.hospital.dto;


import com.hospital.enums.NotificationType;
import com.hospital.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalNotificationResponse {

    private Long id;

    private String title;

    private Role role;

    private String message;

    private NotificationType type;

    private boolean isRead;

    private LocalDateTime createdAt;
}
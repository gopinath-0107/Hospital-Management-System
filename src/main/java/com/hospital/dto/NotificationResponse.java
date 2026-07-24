package com.hospital.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;

    private Long patientId;

    private String patientName;

    private String title;

    private String message;

    private Boolean isRead;

    private LocalDateTime createdAt;

}
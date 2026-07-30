package com.hospital.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoiceConsultationRequest {
    
    private Long appointmentId;
    
    private MultipartFile audio;
}

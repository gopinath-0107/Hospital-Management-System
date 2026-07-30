package com.hospital.service;

import com.hospital.dto.VoiceConsultationResponse;
import org.springframework.web.multipart.MultipartFile;



    public interface VoiceConsultationService {

        VoiceConsultationResponse processVoiceConsultation(
                Long appointmentId,
                MultipartFile audio
        );

    }


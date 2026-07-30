package com.hospital.service;

import com.hospital.dto.VoiceConsultationResponse;

public interface ConsultationParserService {

    VoiceConsultationResponse parse(
            Long appointmentId,
            String transcription
    );

}
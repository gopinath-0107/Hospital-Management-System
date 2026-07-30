package com.hospital.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceConsultationResponse {

    private Long appointmentId;

    private String transcription;

    private String bloodPressure;

    private Double temperature;

    private Integer pulse;

    private String diagnosis;
}

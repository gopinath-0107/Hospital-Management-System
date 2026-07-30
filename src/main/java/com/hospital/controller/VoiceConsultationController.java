package com.hospital.controller;

import com.hospital.dto.VoiceConsultationResponse;
import com.hospital.service.SpeechToTextService;
import com.hospital.service.VoiceConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class VoiceConsultationController {

    private final VoiceConsultationService voiceConsultationService;


    @PostMapping(value = "/voice/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<VoiceConsultationResponse> processVoiceConsultation(

            @RequestParam Long appointmentId,

            @RequestPart("audio") MultipartFile audio

    ) {

        VoiceConsultationResponse response =
                voiceConsultationService.processVoiceConsultation(appointmentId, audio);

        return ResponseEntity.ok(response);
    }

}
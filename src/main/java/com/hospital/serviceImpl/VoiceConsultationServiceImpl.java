package com.hospital.serviceImpl;

import com.hospital.dto.VoiceConsultationResponse;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.enums.AppointmentStatus;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.AppointmentRepository;
import com.hospital.repo.DoctorRepository;
import com.hospital.service.ConsultationParserService;
import com.hospital.service.SpeechToTextService;
import com.hospital.service.VoiceConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VoiceConsultationServiceImpl implements VoiceConsultationService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final SpeechToTextService speechToTextService;
    private final ConsultationParserService consultationParserService;

    @Override
    public VoiceConsultationResponse processVoiceConsultation(
            Long appointmentId,
            MultipartFile audio
    ) {

        // =====================================================
        // 1. Validate Appointment Id
        // =====================================================

        if (appointmentId == null) {
            throw new BadRequestException("Appointment Id is required");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found"));

        // =====================================================
        // 2. Validate Appointment Status
        // =====================================================

        if (appointment.getStatus() != AppointmentStatus.APPROVED) {
            throw new BadRequestException(
                    "Voice consultation is allowed only for APPROVED appointments");
        }

        // =====================================================
        // 3. Get Logged-in Doctor
        // =====================================================

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Doctor loggedInDoctor = doctorRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found"));

        // =====================================================
        // 4. Validate Doctor Ownership
        // =====================================================

        if (!appointment.getDoctor().getId().equals(loggedInDoctor.getId())) {
            throw new AccessDeniedException(
                    "You are not authorized to access this appointment");
        }

        // =====================================================
        // 5. Validate Audio
        // =====================================================

        if (audio == null || audio.isEmpty()) {
            throw new BadRequestException("Audio file is required");
        }

        if (audio.getContentType() == null ||
                !audio.getContentType().startsWith("audio/")) {

            throw new BadRequestException("Only audio files are allowed");
        }

        long maxSize = 25L * 1024L * 1024L;

        if (audio.getSize() > maxSize) {
            throw new BadRequestException("Maximum audio size is 25 MB");
        }

        // =====================================================
        // 6. Convert Audio To Text (Whisper Local)
        // =====================================================

        String transcription = speechToTextService.convertAudioToText(audio);

        // =====================================================
        // 7. Temporary Response
        // =====================================================



        return consultationParserService.parse(
                appointment.getId(),
                transcription
        );
    }
}
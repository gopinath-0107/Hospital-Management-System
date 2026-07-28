package com.hospital.serviceImpl;

import com.hospital.dto.ConsultationResponse;
import com.hospital.dto.CreateConsultationRequest;
import com.hospital.entity.*;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.NotificationType;
import com.hospital.enums.Role;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.exception.UnauthorizedException;
import com.hospital.repo.*;
import com.hospital.service.ConsultationService;

import com.hospital.service.HospitalNotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {


    private final ConsultationRepository consultationRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final SymptomRepository symptomRepository;
    private final HospitalNotificationService hospitalNotificationService;


    @Override
    @Transactional
    public ConsultationResponse createConsultation(
            CreateConsultationRequest request) {

        // 1. Get Logged In Doctor

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Doctor doctor =
                doctorRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found"
                                ));

        // 2. Find Appointment

        Appointment appointment =
                appointmentRepository.findById(
                                request.getAppointmentId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found"
                                ));

        if (consultationRepository.existsByAppointmentId(
                request.getAppointmentId())) {

            throw new DuplicateResourceException(
                    "Consultation already exists"
            );
        }

        if (appointment.getStatus() != AppointmentStatus.APPROVED) {

            throw new BadRequestException(
                    "Consultation can only be created for APPROVED appointments."
            );
        }

        // 3. Check appointment belongs to doctor

        if (!appointment.getDoctor()
                .getId()
                .equals(doctor.getId())) {

            throw new UnauthorizedException(
                    "You are not allowed to consult this appointment"
            );
        }

        if (consultationRepository.existsByAppointmentId(
                request.getAppointmentId())) {

            throw new IllegalStateException(
                    "Consultation already exists for this appointment."
            );
        }

        // 4. Get Symptoms

        List<Symptom> symptoms =
                symptomRepository.findAllById(
                        request.getSymptomIds()
                );

        if (symptoms.isEmpty()) {

            throw new ResourceNotFoundException(
                    "Symptoms not found"
            );
        }

        // 5. Create Consultation

        Consultation consultation = new Consultation();

        consultation.setAppointment(appointment);
        consultation.setDoctor(doctor);
        consultation.setPatient(appointment.getPatient());
        consultation.setSymptoms(symptoms);
        consultation.setBloodPressure(request.getBloodPressure());
        consultation.setTemperature(request.getTemperature());
        consultation.setPulseRate(request.getPulseRate());
        consultation.setDiagnosis(request.getDiagnosis());
        consultation.setNotes(request.getNotes());
        consultation.setCreatedAt(LocalDateTime.now());
        consultation.setUpdatedAt(LocalDateTime.now());

        // 6. Save

        Consultation saved =
                consultationRepository.save(consultation);

        // Consultation Status Update
        appointment.setStatus(AppointmentStatus.CONSULTATION_DONE);
        appointmentRepository.save(appointment);

        // ===========================
        // Notification to Patient
        // ===========================

        hospitalNotificationService.createNotification(
                appointment.getPatient().getId(),
                Role.PATIENT,
                NotificationType.APPOINTMENT,
                "Consultation Completed",
                "Your consultation with Dr. "
                        + doctor.getFirstName() + " "
                        + doctor.getLastName()
                        + " has been completed."
        );

        // 7. Response

        return convertToResponse(saved);
    }



    @Override
    public ConsultationResponse getByAppointmentId(
            Long appointmentId) {


        Consultation consultation =
                consultationRepository
                        .findByAppointmentId(
                                appointmentId
                        );


        if(consultation == null){

            throw new ResourceNotFoundException(
                    "Consultation not found"
            );
        }


        return convertToResponse(
                consultation
        );

    }





    private ConsultationResponse convertToResponse(
            Consultation consultation){


        ConsultationResponse response =
                new ConsultationResponse();



        response.setId(
                consultation.getId()
        );


        response.setAppointmentId(
                consultation.getAppointment()
                        .getId()
        );


        response.setDoctorId(
                consultation.getDoctor()
                        .getId()
        );


        response.setPatientId(
                consultation.getPatient()
                        .getId()
        );


        response.setSymptoms(
                consultation.getSymptoms()
                        .stream()
                        .map(Symptom::getSymptomName)
                        .toList()
        );


        response.setBloodPressure(
                consultation.getBloodPressure()
        );


        response.setTemperature(
                consultation.getTemperature()
        );


        response.setPulseRate(
                consultation.getPulseRate()
        );


        response.setDiagnosis(
                consultation.getDiagnosis()
        );


        response.setNotes(
                consultation.getNotes()
        );


        response.setCreatedAt(
                consultation.getCreatedAt()
        );


        return response;

    }

}
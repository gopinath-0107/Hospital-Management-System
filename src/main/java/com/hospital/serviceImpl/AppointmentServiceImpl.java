package com.hospital.serviceImpl;

import com.hospital.dto.AppointmentRequest;
import com.hospital.dto.AppointmentResponse;
import com.hospital.entity.*;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.NotificationType;
import com.hospital.enums.Role;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.*;
import com.hospital.service.AppointmentService;
import com.hospital.service.HospitalNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.hospital.enums.AvailabilityStatus;
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final HospitalNotificationService hospitalNotificationService;
    private final ReceptionistRepository receptionistRepository;

    @Override
    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with ID: " + request.getPatientId()
                        ));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with ID: " + request.getDoctorId()
                        ));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with ID: " + request.getDepartmentId()
                        ));


        /*
         * Doctor Availability Check
         */

        DoctorAvailability availability =
                doctorAvailabilityRepository
                        .findByDoctorIdAndAvailableDate(
                                doctor.getId(),
                                request.getAppointmentDate().toLocalDate()
                        )
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "Doctor is not available on selected date."
                                ));

        if (availability.getStatus() != AvailabilityStatus.AVAILABLE) {

            throw new BadRequestException(
                    "Doctor is "
                            + availability.getStatus()
                            + " on selected date."
            );
        }


        /*
         * Hospital Timing Validation
         * 10 AM - 8 PM
         */

        LocalDateTime appointmentDate = request.getAppointmentDate();

        LocalTime appointmentTime = appointmentDate.toLocalTime();


        LocalTime openingTime = availability.getStartTime();
        LocalTime closingTime = availability.getEndTime();


        if (appointmentTime.isBefore(openingTime)
                || !appointmentTime.isBefore(closingTime)) {

            throw new BadRequestException(
                    "Appointment time is outside doctor's available timing."
            );
        }


        /*
         * Break Time Validation
         * 2 PM - 3 PM
         */

        LocalTime breakStart = LocalTime.of(14, 0);
        LocalTime breakEnd = LocalTime.of(15, 0);


        if (!appointmentTime.isBefore(breakStart)
                && appointmentTime.isBefore(breakEnd)) {

            throw new BadRequestException(
                    "Hospital break time is between 2 PM to 3 PM"
            );
        }



        /*
         * 20 Minutes Slot Validation
         */

        int minute = appointmentTime.getMinute();


        if (minute != 0 && minute != 20 && minute != 40) {

            throw new BadRequestException(
                    "Appointment should be booked only on 20 minute slots"
            );
        }


        boolean pendingAppointment =
                appointmentRepository.existsByPatientIdAndStatus(
                        patient.getId(),
                        AppointmentStatus.PENDING
                );

        if (pendingAppointment) {

            throw new BadRequestException(
                    "You already have a pending appointment."
            );
        }

        LocalDate appointmentDay = appointmentDate.toLocalDate();

        boolean patientAlreadyBooked =
                appointmentRepository
                        .existsByPatientIdAndAppointmentDateBetween(
                                patient.getId(),
                                appointmentDay.atStartOfDay(),
                                appointmentDay.atTime(23,59,59)
                        );

        if(patientAlreadyBooked){

            throw new RuntimeException(
                    "Patient already has an appointment on this date."
            );
        }

        long doctorAppointmentCount =
                appointmentRepository
                        .countByDoctorIdAndAppointmentDateBetween(
                                doctor.getId(),
                                appointmentDay.atStartOfDay(),
                                appointmentDay.atTime(23, 59, 59)
                        );

        if (doctorAppointmentCount >= 27) {

            throw new BadRequestException(
                    "Doctor appointment limit reached for today."
            );
        }

        /*
         * Duplicate Slot Check
         * Same Doctor + Same Date + Same Time
         */

        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDate(
                                doctor.getId(),
                                appointmentDate
                        );


        if (alreadyBooked) {

            throw new BadRequestException(
                    "This appointment slot is already booked"
            );
        }



        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .department(department)
                .appointmentDate(request.getAppointmentDate())
                .symptoms(request.getSymptoms())
                .status(AppointmentStatus.PENDING)
                .build();


        Appointment saved = appointmentRepository.save(appointment);

        // =========================
        // Notification to Receptionists
        // =========================

        List<Receptionist> receptionists =
                receptionistRepository.findAll();

        for (Receptionist receptionist : receptionists) {

            hospitalNotificationService.createNotification(
                    receptionist.getId(),
                    Role.RECEPTIONIST,
                    NotificationType.APPOINTMENT,
                    "New Appointment",
                    "New appointment booked by "
                            + patient.getFirstName() + " "
                            + patient.getLastName()
            );
        }

        return mapToResponse(saved);
    }
    @Override
    @Transactional
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {

            throw new RuntimeException(
                    "Appointment already completed"
            );
        }
        appointment.setStatus(status);
        Appointment updated = appointmentRepository.save(appointment);
        return mapToResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));
        return mapToResponse(appointment);
    }

    private AppointmentResponse mapToResponse(Appointment app) {
        return AppointmentResponse.builder()
                .id(app.getId())
                .patientId(app.getPatient().getId())
                .patientName(app.getPatient().getFirstName() + " " + app.getPatient().getLastName())
                .doctorId(app.getDoctor().getId())
                .doctorName(app.getDoctor().getFirstName() + " " + app.getDoctor().getLastName())
                .departmentId(app.getDepartment().getId())
                .departmentName(app.getDepartment().getDepartmentName())
                .appointmentDate(app.getAppointmentDate())
                .symptoms(app.getSymptoms())
                .status(app.getStatus())
                .createdAt(app.getCreatedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }


    @Override
    @Transactional
    public AppointmentResponse approveAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with ID: " + appointmentId));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {

            throw new RuntimeException(
                    "Appointment already completed"
            );
        }

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING appointments can be approved");
        }

        appointment.setStatus(AppointmentStatus.APPROVED);

        Appointment saved = appointmentRepository.save(appointment);

        // ===========================
        // Notification to Patient
        // ===========================

        hospitalNotificationService.createNotification(
                appointment.getPatient().getId(),
                Role.PATIENT,
                NotificationType.APPOINTMENT,
                "Appointment Approved",
                "Your appointment with Dr. "
                        + appointment.getDoctor().getFirstName() + " "
                        + appointment.getDoctor().getLastName()
                        + " has been approved."
        );

        // ===========================
        // Notification to Doctor
        // ===========================

        hospitalNotificationService.createNotification(
                appointment.getDoctor().getId(),
                Role.DOCTOR,
                NotificationType.APPOINTMENT,
                "New Patient Appointment",
                "You have a new approved appointment with "
                        + appointment.getPatient().getFirstName() + " "
                        + appointment.getPatient().getLastName()
        );

        return mapToResponse(saved);
    }
    @Override
    @Transactional
    public AppointmentResponse rejectAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with ID: " + appointmentId));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {

            throw new RuntimeException(
                    "Appointment already completed"
            );
        }

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING appointments can be rejected");
        }

        appointment.setStatus(AppointmentStatus.REJECTED);

        Appointment saved = appointmentRepository.save(appointment);

        return mapToResponse(saved);
    }


    @Override
    @Transactional
    public AppointmentResponse completeAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with ID: " + appointmentId));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {

            throw new RuntimeException(
                    "Appointment already completed"
            );
        }

        if (appointment.getStatus() != AppointmentStatus.CONSULTATION_DONE) {
            throw new IllegalStateException(
                    "Only CONSULTATION_DONE appointments can be completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        Appointment saved = appointmentRepository.save(appointment);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public AppointmentResponse consultationCompleted(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with ID: " + appointmentId));

        if (appointment.getStatus() != AppointmentStatus.APPROVED) {
            throw new IllegalStateException(
                    "Only APPROVED appointments can start consultation");
        }

        appointment.setStatus(AppointmentStatus.CONSULTATION_DONE);

        Appointment saved = appointmentRepository.save(appointment);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getPendingAppointmentsByDoctor(Long doctorId) {

        return appointmentRepository
                .findByDoctorIdAndStatus(
                        doctorId,
                        AppointmentStatus.PENDING
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getApprovedAppointmentsByDoctor(Long doctorId) {

        return appointmentRepository
                .findByDoctorIdAndStatus(
                        doctorId,
                        AppointmentStatus.APPROVED
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getCompletedAppointmentsByDoctor(Long doctorId) {

        return appointmentRepository
                .findByDoctorIdAndStatus(
                        doctorId,
                        AppointmentStatus.COMPLETED
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalDateTime> getAvailableSlots(
            Long doctorId,
            LocalDate date
    ) {

        List<LocalDateTime> availableSlots = new ArrayList<>();

        // Check Doctor Availability
        DoctorAvailability availability =
                doctorAvailabilityRepository
                        .findByDoctorIdAndAvailableDate(
                                doctorId,
                                date
                        )
                        .orElse(null);

        if (availability == null) {
            return availableSlots;
        }

        if (availability.getStatus() != AvailabilityStatus.AVAILABLE) {
            return availableSlots;
        }

        // Doctor working timing
        LocalTime startTime = availability.getStartTime();
        LocalTime endTime = availability.getEndTime();

        while (startTime.isBefore(endTime)) {

            // Skip Lunch Break (2 PM - 3 PM)
            if (startTime.isBefore(LocalTime.of(14, 0))
                    || !startTime.isBefore(LocalTime.of(15, 0))) {

                availableSlots.add(
                        LocalDateTime.of(date, startTime)
                );
            }

            startTime = startTime.plusMinutes(20);
        }

        // Already booked appointments
        List<Appointment> bookedAppointments =
                appointmentRepository.findByDoctorIdAndAppointmentDateBetween(
                        doctorId,
                        date.atStartOfDay(),
                        date.atTime(23, 59, 59)
                );

        // Remove booked slots
        for (Appointment appointment : bookedAppointments) {
            availableSlots.remove(
                    appointment.getAppointmentDate()
            );
        }

        return availableSlots;
    }
}

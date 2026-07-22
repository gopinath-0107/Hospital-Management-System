package com.hospital.serviceImpl;

import com.hospital.dto.AppointmentRequest;
import com.hospital.dto.AppointmentResponse;
import com.hospital.entity.Appointment;
import com.hospital.entity.Department;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.enums.AppointmentStatus;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.AppointmentRepository;
import com.hospital.repo.DepartmentRepository;
import com.hospital.repo.DoctorRepository;
import com.hospital.repo.PatientRepository;
import com.hospital.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

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
         * Hospital Timing Validation
         * 10 AM - 8 PM
         */

        LocalDateTime appointmentDate = request.getAppointmentDate();

        LocalTime appointmentTime = appointmentDate.toLocalTime();


        LocalTime openingTime = LocalTime.of(10, 0);
        LocalTime closingTime = LocalTime.of(20, 0);


        if (appointmentTime.isBefore(openingTime)
                || !appointmentTime.isBefore(closingTime)) {

            throw new RuntimeException(
                    "Hospital timing is between 10 AM to 8 PM"
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

            throw new RuntimeException(
                    "Hospital break time is between 2 PM to 3 PM"
            );
        }



        /*
         * 20 Minutes Slot Validation
         */

        int minute = appointmentTime.getMinute();


        if (minute != 0 && minute != 20 && minute != 40) {

            throw new RuntimeException(
                    "Appointment should be booked only on 20 minute slots"
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

            throw new RuntimeException(
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


        // Hospital timing
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);


        while(startTime.isBefore(endTime)){


            // Skip break 2 PM - 3 PM

            if(startTime.isBefore(LocalTime.of(14,0))
                    || !startTime.isBefore(LocalTime.of(15,0))) {


                availableSlots.add(
                        LocalDateTime.of(date,startTime)
                );
            }


            // next 20 minutes slot

            startTime = startTime.plusMinutes(20);
        }



        // Database मधून booked appointments घेणे

        List<Appointment> bookedAppointments =
                appointmentRepository
                        .findByDoctorIdAndAppointmentDateBetween(
                                doctorId,
                                date.atStartOfDay(),
                                date.atTime(23,59)
                        );



        // Booked time remove करणे

        for(Appointment appointment : bookedAppointments){

            availableSlots.remove(
                    appointment.getAppointmentDate()
            );
        }


        return availableSlots;
    }
}

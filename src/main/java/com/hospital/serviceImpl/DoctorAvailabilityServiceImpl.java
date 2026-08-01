package com.hospital.serviceImpl;

import com.hospital.dto.DoctorAvailabilityRequest;
import com.hospital.dto.DoctorAvailabilityResponse;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.DoctorAvailability;
import com.hospital.entity.Notification;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.AvailabilityStatus;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.AppointmentRepository;
import com.hospital.repo.DoctorAvailabilityRepository;
import com.hospital.repo.DoctorRepository;
import com.hospital.repo.NotificationRepository;
import com.hospital.service.DoctorAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityServiceImpl
        implements DoctorAvailabilityService {

    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final DoctorRepository doctorRepository;

    private final AppointmentRepository appointmentRepository;

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public DoctorAvailabilityResponse createAvailability(
            DoctorAvailabilityRequest request) {

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id : " + request.getDoctorId()));



        validateRequest(request);

        boolean exists = doctorAvailabilityRepository
                .existsByDoctorIdAndAvailableDate(
                        doctor.getId(),
                        request.getAvailableDate());

        if (exists) {
            throw new BadRequestException(
                    "Availability already exists for this doctor on selected date.");
        }

        DoctorAvailability availability = DoctorAvailability.builder()
                .doctor(doctor)
                .availableDate(request.getAvailableDate())
                .status(request.getStatus())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        DoctorAvailability saved = doctorAvailabilityRepository.save(availability);

        return mapToResponse(saved);
    }
    @Override
    @Transactional
    public DoctorAvailabilityResponse updateAvailability(
            Long id,
            DoctorAvailabilityRequest request) {

        DoctorAvailability availability =
                doctorAvailabilityRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Availability not found"));

        Doctor doctor =
                doctorRepository.findById(request.getDoctorId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found"));

        validateRequest(request);

        availability.setDoctor(doctor);
        availability.setAvailableDate(request.getAvailableDate());
        availability.setStatus(request.getStatus());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());

        DoctorAvailability updated =
                doctorAvailabilityRepository.save(availability);

        return mapToResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorAvailabilityResponse getAvailability(Long id) {

        DoctorAvailability availability =
                doctorAvailabilityRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Availability not found"));

        return mapToResponse(availability);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorAvailabilityResponse> getDoctorAvailability(
            Long doctorId) {

        List<DoctorAvailability> list =
                doctorAvailabilityRepository.findByDoctorId(doctorId);

        List<DoctorAvailabilityResponse> response =
                new ArrayList<>();

        for (DoctorAvailability availability : list) {

            response.add(
                    mapToResponse(availability)
            );
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorAvailabilityResponse> getAvailabilityByDate(
            LocalDate date) {

        List<DoctorAvailability> list =
                doctorAvailabilityRepository.findByAvailableDate(date);

        List<DoctorAvailabilityResponse> response =
                new ArrayList<>();

        for (DoctorAvailability availability : list) {

            response.add(
                    mapToResponse(availability)
            );
        }

        return response;
    }

    @Override
    @Transactional
    public void deleteAvailability(Long id) {

        DoctorAvailability availability =
                doctorAvailabilityRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Availability not found"));

        doctorAvailabilityRepository.delete(availability);
    }

    // ===================================================
    // Validation
    // ===================================================

    private void validateRequest(
            DoctorAvailabilityRequest request) {

        if (request.getAvailableDate().isBefore(LocalDate.now())) {

            throw new RuntimeException(
                    "Availability date cannot be in past."
            );
        }

        if (request.getStatus() == AvailabilityStatus.AVAILABLE) {

            if (request.getStartTime() == null
                    || request.getEndTime() == null) {

                throw new RuntimeException(
                        "Start Time and End Time are required."
                );
            }

            if (!request.getStartTime()
                    .isBefore(request.getEndTime())) {

                throw new RuntimeException(
                        "Start time must be before End time."
                );
            }

            LocalTime hospitalStart =
                    LocalTime.of(10, 0);

            LocalTime hospitalEnd =
                    LocalTime.of(20, 0);

            if (request.getStartTime().isBefore(hospitalStart)
                    || request.getEndTime().isAfter(hospitalEnd)) {

                throw new RuntimeException(
                        "Hospital timing is 10 AM to 8 PM."
                );
            }

        }

    }


    @Override
    @Transactional
    public DoctorAvailabilityResponse markDoctorEmergency(Long doctorId) {

        LocalDate today = LocalDate.now();

        DoctorAvailability availability =
                doctorAvailabilityRepository
                        .findByDoctorIdAndAvailableDate(
                                doctorId,
                                today
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor availability not found."
                                ));

        availability.setStatus(AvailabilityStatus.EMERGENCY);

        doctorAvailabilityRepository.save(availability);

        LocalDateTime currentTime = LocalDateTime.now();

        List<AppointmentStatus> statuses = List.of(
                AppointmentStatus.PENDING,
                AppointmentStatus.APPROVED
        );

        List<Appointment> appointments =
                appointmentRepository
                        .findByDoctorIdAndAppointmentDateGreaterThanEqualAndStatusIn(
                                doctorId,
                                currentTime,
                                statuses
                        );

        for (Appointment appointment : appointments) {

            appointment.setStatus(AppointmentStatus.CANCELLED);

            appointmentRepository.save(appointment);

            Notification notification = Notification.builder()
                    .patient(appointment.getPatient())
                    .title("Appointment Cancelled")
                    .message(
                            "Your appointment has been cancelled because the doctor is unavailable due to an emergency. Please book another appointment."
                    )
                    .build();

            notificationRepository.save(notification);
        }

        return mapToResponse(availability);
    }

    // ===================================================
    // Mapper
    // ===================================================

    private DoctorAvailabilityResponse mapToResponse(
            DoctorAvailability availability) {

        return DoctorAvailabilityResponse.builder()
                .id(availability.getId())
                .doctorId(availability.getDoctor().getId())
                .doctorName(
                        availability.getDoctor().getFirstName()
                                + " "
                                + availability.getDoctor().getLastName()
                )
                .availableDate(
                        availability.getAvailableDate()
                )
                .status(
                        availability.getStatus()
                )
                .startTime(
                        availability.getStartTime()
                )
                .endTime(
                        availability.getEndTime()
                )
                .createdAt(
                        availability.getCreatedAt()
                )
                .updatedAt(
                        availability.getUpdatedAt()
                )
                .build();
    }

}
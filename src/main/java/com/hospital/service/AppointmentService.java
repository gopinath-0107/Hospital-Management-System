package com.hospital.service;

import com.hospital.dto.AppointmentRequest;
import com.hospital.dto.AppointmentResponse;
import com.hospital.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    AppointmentResponse bookAppointment(AppointmentRequest request);

    AppointmentResponse updateAppointmentStatus(Long appointmentId,
                                                AppointmentStatus status);

    List<LocalDateTime> getAvailableSlots(
            Long doctorId,
            LocalDate date
    );

    AppointmentResponse approveAppointment(Long appointmentId);

    AppointmentResponse rejectAppointment(Long appointmentId);

    AppointmentResponse completeAppointment(Long appointmentId);

    AppointmentResponse consultationCompleted(Long appointmentId);

    AppointmentResponse getAppointmentById(Long appointmentId);

    List<AppointmentResponse> getAppointmentsByPatientId(Long patientId);

    List<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId);

    List<AppointmentResponse> getPendingAppointmentsByDoctor(Long doctorId);

    List<AppointmentResponse> getApprovedAppointmentsByDoctor(Long doctorId);

    List<AppointmentResponse> getCompletedAppointmentsByDoctor(Long doctorId);

}
package com.hospital.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @NotNull(message = "Patient ID is required")
    @Positive(message = "Patient ID must be greater than 0")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    @Positive(message = "Doctor ID must be greater than 0")
    private Long doctorId;

    @NotNull(message = "Department ID is required")
    @Positive(message = "Department ID must be greater than 0")
    private Long departmentId;

    @NotNull(message = "Appointment date and time is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;



}
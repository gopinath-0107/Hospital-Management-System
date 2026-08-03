package com.hospital.dto;

import com.hospital.enums.AvailabilityStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DoctorAvailabilityRequest {

    @NotNull(message = "Doctor Id is required")
    private Long doctorId;

    @NotNull(message = "Available Date is required")
    private LocalDate availableDate;

    @NotNull(message = "Status is required")
    private AvailabilityStatus status;

    private LocalTime startTime;

    private LocalTime endTime;

}
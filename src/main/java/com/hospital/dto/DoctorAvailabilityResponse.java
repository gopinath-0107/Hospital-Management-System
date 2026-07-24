package com.hospital.dto;

import com.hospital.enums.AvailabilityStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class DoctorAvailabilityResponse {

    private Long id;

    private Long doctorId;

    private String doctorName;

    private LocalDate availableDate;

    private AvailabilityStatus status;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
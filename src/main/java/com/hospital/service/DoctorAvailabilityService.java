package com.hospital.service;

import com.hospital.dto.DoctorAvailabilityRequest;
import com.hospital.dto.DoctorAvailabilityResponse;

import java.time.LocalDate;
import java.util.List;

public interface DoctorAvailabilityService {

    DoctorAvailabilityResponse createAvailability(
            DoctorAvailabilityRequest request
    );

    DoctorAvailabilityResponse updateAvailability(
            Long id,
            DoctorAvailabilityRequest request
    );

    DoctorAvailabilityResponse getAvailability(
            Long id
    );

    List<DoctorAvailabilityResponse> getDoctorAvailability(
            Long doctorId
    );

    List<DoctorAvailabilityResponse> getAvailabilityByDate(
            LocalDate date
    );

    void deleteAvailability(
            Long id
    );

    DoctorAvailabilityResponse markDoctorEmergency(Long doctorId);

}
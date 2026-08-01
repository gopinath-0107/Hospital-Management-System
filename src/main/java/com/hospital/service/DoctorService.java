package com.hospital.service;

import com.hospital.dto.DoctorResponse;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {
    DoctorResponse getDoctorProfile(Long id);

    List<DoctorResponse> getAllDoctors();

    List<DoctorResponse> getDoctors(
            Long departmentId,
            Long specializationId
    );

    List<DoctorResponse> getAvailableDoctors(
            LocalDate date,
            Long departmentId,
            Long specializationId
    );
}

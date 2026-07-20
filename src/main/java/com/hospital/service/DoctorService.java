package com.hospital.service;

import com.hospital.dto.DoctorResponse;
import java.util.List;

public interface DoctorService {
    DoctorResponse getDoctorProfile(Long id);
    List<DoctorResponse> getAvailableDoctorsByDepartment(Long departmentId);
}

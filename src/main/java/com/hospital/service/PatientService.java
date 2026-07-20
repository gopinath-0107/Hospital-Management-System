package com.hospital.service;

import com.hospital.dto.UserResponse;
import java.util.List;

public interface PatientService {
    UserResponse getPatientProfile(Long id);
    List<UserResponse> searchPatients(String query);
}

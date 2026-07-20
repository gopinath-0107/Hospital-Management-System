package com.hospital.service;

import com.hospital.dto.DoctorResponse;
import com.hospital.dto.request.DoctorRegistrationRequest;
import com.hospital.dto.request.LoginRequest;
import com.hospital.dto.request.PatientRegistrationRequest;
import com.hospital.dto.response.*;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    void createDefaultAdmin();


    ApiResponse<PatientResponse> registerPatient(PatientRegistrationRequest request);
    ApiResponse<DoctorResponse> registerDoctor(DoctorRegistrationRequest request);
}

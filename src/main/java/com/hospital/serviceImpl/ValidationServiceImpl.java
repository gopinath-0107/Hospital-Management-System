package com.hospital.serviceImpl;

import com.hospital.exception.DuplicateResourceException;
import com.hospital.repo.*;
import com.hospital.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final NurseRepository nurseRepository;
    private final ReceptionistRepository receptionistRepository;

    @Override
    public void validateEmailAndMobile(String email, String mobile) {

        // Email Validation
        if (adminRepository.existsByEmail(email)
                || doctorRepository.existsByEmail(email)
                || patientRepository.existsByEmail(email)
                || nurseRepository.existsByEmail(email)
                || receptionistRepository.existsByEmail(email)) {

            throw new DuplicateResourceException("Email already exists");
        }

        // Mobile Validation
        if (doctorRepository.existsByMobile(mobile)
                || patientRepository.existsByMobile(mobile)
                || nurseRepository.existsByMobile(mobile)
                || receptionistRepository.existsByMobile(mobile)) {

            throw new DuplicateResourceException("Mobile number already exists");
        }
    }
}
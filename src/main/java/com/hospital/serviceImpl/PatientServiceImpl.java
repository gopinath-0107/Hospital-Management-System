package com.hospital.serviceImpl;

import com.hospital.dto.UserResponse;
import com.hospital.entity.Patient;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.PatientRepository;
import com.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getPatientProfile(Long id) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
        return mapToResponse(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchPatients(String query) {
        return patientRepository.searchPatients(query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToResponse(Patient p) {
        return UserResponse.builder()
                .id(p.getId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .mobile(p.getMobile())
                .role(p.getRole().name())
                .status(p.getStatus().name())
                .additionalDetails("DOB: " + p.getDateOfBirth() + ", Blood: " + p.getBloodGroup() + ", City: " + p.getCity())
                .build();
    }
}

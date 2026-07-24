package com.hospital.serviceImpl;

import com.hospital.dto.DoctorResponse;
import com.hospital.entity.Doctor;
import com.hospital.enums.Status;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.DoctorRepository;
import com.hospital.repo.SpecializationRepository;
import com.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorProfile(Long id) {
        Doctor d = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
        return mapToResponse(d);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAvailableDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentIdAndAvailableAndStatus(departmentId, true, Status.ACTIVE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private DoctorResponse mapToResponse(Doctor d) {
        return DoctorResponse.builder()
                .id(d.getId())
                .firstName(d.getFirstName())
                .lastName(d.getLastName())
                .email(d.getEmail())
                .mobile(d.getMobile())
                .departmentName(d.getDepartment().getDepartmentName())
                .qualification(d.getQualification())
                .experience(d.getExperience())
                .specializationId(d.getSpecialization().getId())
                .specializationName(d.getSpecialization().getSpecializationName())
                .consultationFee(d.getConsultationFee())
                .available(d.getAvailable())
                .profileImage(d.getProfileImage())
                .role(d.getRole())
                .status(d.getStatus())
                .build();
    }
}

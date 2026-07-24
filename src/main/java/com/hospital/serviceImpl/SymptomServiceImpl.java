package com.hospital.serviceImpl;

import com.hospital.dto.*;
import com.hospital.entity.Department;
import com.hospital.entity.Doctor;
import com.hospital.entity.Symptom;
import com.hospital.enums.Status;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.DepartmentRepository;
import com.hospital.repo.DoctorRepository;
import com.hospital.repo.SymptomRepository;
import com.hospital.service.SymptomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SymptomServiceImpl implements SymptomService {

    private final SymptomRepository symptomRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public SymptomResponse createSymptom(SymptomRequest request) {
        if (symptomRepository.existsBySymptomName(request.getSymptomName())) {
            throw new DuplicateResourceException("Symptom '" + request.getSymptomName() + "' already exists.");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.getDepartmentId()));

        Symptom symptom = Symptom.builder()
                .symptomName(request.getSymptomName())
                .department(department)
                .build();

        Symptom saved = symptomRepository.save(symptom);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentSuggestResponse suggestDepartmentAndDoctors(SymptomSuggestRequest request) {
        List<Symptom> symptoms = symptomRepository.findBySymptomNameIn(request.getSymptomNames());
        if (symptoms.isEmpty()) {
            throw new BadRequestException("No registered symptoms found for the selected symptom names.");
        }

        // Determine matching department by maximum frequency count among matching symptoms
        Map<Department, Long> frequencyMap = symptoms.stream()
                .collect(Collectors.groupingBy(Symptom::getDepartment, Collectors.counting()));

        Department suggestedDepartment = frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new BadRequestException("Could not determine department."));

        // Fetch available and active doctors for the suggested department
        List<Doctor> doctors = doctorRepository.findByDepartmentIdAndAvailableAndStatus(
                suggestedDepartment.getId(), true, Status.ACTIVE);

        List<DoctorResponse> doctorResponses = doctors.stream()
                .map(d -> DoctorResponse.builder()
                        .id(d.getId())
                        .firstName(d.getFirstName())
                        .lastName(d.getLastName())
                        .email(d.getEmail())
                        .mobile(d.getMobile())
                        .departmentName(suggestedDepartment.getDepartmentName())
                        .qualification(d.getQualification())
                        .experience(d.getExperience())
                        .specializationId(d.getSpecialization().getId())
                        .specializationName(d.getSpecialization().getSpecializationName())
                        .consultationFee(d.getConsultationFee())
                        .available(d.getAvailable())
                        .profileImage(d.getProfileImage())
                        .role(d.getRole())
                        .status(d.getStatus())
                        .build())
                .collect(Collectors.toList());

        return DepartmentSuggestResponse.builder()
                .departmentId(suggestedDepartment.getId())
                .departmentName(suggestedDepartment.getDepartmentName())
                .floorNumber(suggestedDepartment.getFloorNumber())
                .availableDoctors(doctorResponses)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SymptomResponse> getAllSymptoms() {
        return symptomRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SymptomResponse mapToResponse(Symptom symptom) {
        return SymptomResponse.builder()
                .id(symptom.getId())
                .symptomName(symptom.getSymptomName())
                .departmentId(symptom.getDepartment().getId())
                .departmentName(symptom.getDepartment().getDepartmentName())
                .build();
    }
}

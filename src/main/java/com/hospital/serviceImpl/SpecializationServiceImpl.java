package com.hospital.serviceImpl;

import com.hospital.dto.SpecializationRequest;
import com.hospital.dto.SpecializationResponse;
import com.hospital.entity.Department;
import com.hospital.entity.Specialization;
import com.hospital.enums.Status;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.DepartmentRepository;
import com.hospital.repo.SpecializationRepository;
import com.hospital.service.SpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository specializationRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public SpecializationResponse createSpecialization(
            SpecializationRequest request
    ) {

        Department department =
                departmentRepository.findById(request.getDepartmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found with ID : "
                                                + request.getDepartmentId()
                                ));

        boolean exists =
                specializationRepository
                        .existsByDepartmentIdAndSpecializationName(
                                department.getId(),
                                request.getSpecializationName()
                        );

        if (exists) {

            throw new RuntimeException(
                    "Specialization already exists in this department."
            );
        }

        Specialization specialization =
                Specialization.builder()
                        .specializationName(request.getSpecializationName())
                        .department(department)
                        .status(Status.ACTIVE)
                        .build();

        Specialization saved =
                specializationRepository.save(specialization);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public SpecializationResponse updateSpecialization(
            Long id,
            SpecializationRequest request
    ) {

        Specialization specialization =
                specializationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Specialization not found."
                                ));

        Department department =
                departmentRepository.findById(request.getDepartmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found."
                                ));

        boolean exists =
                specializationRepository
                        .existsByDepartmentIdAndSpecializationName(
                                department.getId(),
                                request.getSpecializationName()
                        );

        if (exists &&
                !specialization.getSpecializationName()
                        .equalsIgnoreCase(request.getSpecializationName())) {

            throw new RuntimeException(
                    "Specialization already exists."
            );
        }

        specialization.setSpecializationName(
                request.getSpecializationName()
        );

        specialization.setDepartment(department);

        Specialization updated =
                specializationRepository.save(specialization);

        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteSpecialization(Long id) {

        Specialization specialization =
                specializationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Specialization not found."
                                ));

        specialization.setStatus(Status.INACTIVE);

        specializationRepository.save(specialization);
    }

    @Override
    @Transactional(readOnly = true)
    public SpecializationResponse getSpecializationById(Long id) {

        Specialization specialization =
                specializationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Specialization not found."
                                ));

        return mapToResponse(specialization);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecializationResponse> getAllSpecializations() {

        return specializationRepository.findByStatus(Status.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecializationResponse> getSpecializationsByDepartment(
            Long departmentId
    ) {

        return specializationRepository
                .findByDepartmentIdAndStatus(
                        departmentId,
                        Status.ACTIVE
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SpecializationResponse mapToResponse(
            Specialization specialization
    ) {

        return SpecializationResponse.builder()
                .id(specialization.getId())
                .specializationName(
                        specialization.getSpecializationName()
                )
                .departmentId(
                        specialization.getDepartment().getId()
                )
                .departmentName(
                        specialization.getDepartment().getDepartmentName()
                )
                .status(specialization.getStatus())
                .createdAt(specialization.getCreatedAt())
                .updatedAt(specialization.getUpdatedAt())
                .build();
    }

}
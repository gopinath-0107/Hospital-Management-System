package com.hospital.service;

import com.hospital.dto.SpecializationRequest;
import com.hospital.dto.SpecializationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SpecializationService {

    SpecializationResponse createSpecialization(
            SpecializationRequest request
    );

    void importSpecializations(MultipartFile file);

    SpecializationResponse updateSpecialization(
            Long id,
            SpecializationRequest request
    );

    void deleteSpecialization(Long id);

    SpecializationResponse getSpecializationById(Long id);

    List<SpecializationResponse> getAllSpecializations();

    List<SpecializationResponse> getSpecializationsByDepartment(
            Long departmentId
    );

}
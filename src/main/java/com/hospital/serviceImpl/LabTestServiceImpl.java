package com.hospital.serviceImpl;

import com.hospital.dto.CreateLabTestRequest;
import com.hospital.dto.LabTestResponse;
import com.hospital.entity.LabTest;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.LabTestRepository;
import com.hospital.service.LabTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabTestServiceImpl implements LabTestService {

    private final LabTestRepository labTestRepository;

    @Override
    @Transactional
    public LabTestResponse createLabTest(CreateLabTestRequest request) {

        if (labTestRepository.existsByTestNameIgnoreCase(request.getTestName().trim())) {
            throw new DuplicateResourceException("Lab Test already exists.");
        }

        LabTest labTest = LabTest.builder()
                .testName(request.getTestName().trim())
                .description(request.getDescription())
                .price(request.getPrice())
                .active(true)
                .build();

        LabTest saved = labTestRepository.save(labTest);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LabTestResponse getLabTestById(Long id) {

        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lab Test not found with ID : " + id));

        return mapToResponse(labTest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestResponse> getAllLabTests() {

        return labTestRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LabTestResponse updateLabTest(Long id,
                                         CreateLabTestRequest request) {

        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lab Test not found with ID : " + id));

        if (!labTest.getTestName().equalsIgnoreCase(request.getTestName().trim())
                && labTestRepository.existsByTestNameIgnoreCase(request.getTestName().trim())) {

            throw new DuplicateResourceException("Lab Test already exists.");
        }

        labTest.setTestName(request.getTestName().trim());
        labTest.setDescription(request.getDescription());
        labTest.setPrice(request.getPrice());

        LabTest updated = labTestRepository.save(labTest);

        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteLabTest(Long id) {

        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lab Test not found with ID : " + id));

        labTest.setActive(false);

        labTestRepository.save(labTest);
    }

    private LabTestResponse mapToResponse(LabTest labTest) {

        return LabTestResponse.builder()
                .id(labTest.getId())
                .testName(labTest.getTestName())
                .description(labTest.getDescription())
                .price(labTest.getPrice())
                .active(labTest.getActive())
                .createdAt(labTest.getCreatedAt())
                .updatedAt(labTest.getUpdatedAt())
                .build();
    }

}
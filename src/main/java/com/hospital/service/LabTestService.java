package com.hospital.service;

import com.hospital.dto.CreateLabTestRequest;
import com.hospital.dto.LabTestResponse;

import java.util.List;

public interface LabTestService {

    LabTestResponse createLabTest(CreateLabTestRequest request);

    LabTestResponse getLabTestById(Long id);

    List<LabTestResponse> getAllLabTests();

    LabTestResponse updateLabTest(Long id, CreateLabTestRequest request);

    void deleteLabTest(Long id);

}
package com.hospital.service;

import com.hospital.dto.DepartmentSuggestResponse;
import com.hospital.dto.SymptomRequest;
import com.hospital.dto.SymptomResponse;
import com.hospital.dto.SymptomSuggestRequest;

import java.util.List;

public interface SymptomService {
    SymptomResponse createSymptom(SymptomRequest request);
    DepartmentSuggestResponse suggestDepartmentAndDoctors(SymptomSuggestRequest request);
    List<SymptomResponse> getAllSymptoms();
}

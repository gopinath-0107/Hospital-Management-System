package com.hospital.service;


import com.hospital.dto.CreateLabTechnicianRequest;
import com.hospital.dto.LabTechnicianResponse;

import java.util.List;


public interface LabTechnicianService {


    LabTechnicianResponse createLabTechnician(
            CreateLabTechnicianRequest request);


    LabTechnicianResponse getLabTechnicianById(Long id);


    List<LabTechnicianResponse> getAllLabTechnicians();


    LabTechnicianResponse updateLabTechnician(
            Long id,
            CreateLabTechnicianRequest request);


    void deleteLabTechnician(Long id);

}
package com.hospital.service;

import com.hospital.dto.CreateNurseRequest;
import com.hospital.dto.NurseResponse;

import java.util.List;

public interface NurseService {

    NurseResponse createNurse(CreateNurseRequest request);

    NurseResponse getNurseById(Long id);

    List<NurseResponse> getAllNurses();

    NurseResponse updateNurse(Long id, CreateNurseRequest request);

    void deleteNurse(Long id);
}
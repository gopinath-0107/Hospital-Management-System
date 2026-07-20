package com.hospital.service;


import com.hospital.dto.CreateReceptionistRequest;
import com.hospital.dto.ReceptionistResponse;

import java.util.List;


public interface ReceptionistService {


    ReceptionistResponse createReceptionist(
            CreateReceptionistRequest request);


    ReceptionistResponse getReceptionistById(Long id);


    List<ReceptionistResponse> getAllReceptionists();


    ReceptionistResponse updateReceptionist(
            Long id,
            CreateReceptionistRequest request);


    void deleteReceptionist(Long id);
}
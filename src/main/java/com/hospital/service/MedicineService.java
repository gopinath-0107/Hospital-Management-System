package com.hospital.service;

import com.hospital.dto.MedicineRequest;
import com.hospital.dto.MedicineResponse;
import com.hospital.dto.StockUpdateRequest;

import java.util.List;

public interface MedicineService {

    MedicineResponse addMedicine(MedicineRequest request);

    MedicineResponse updateMedicine(Long id, MedicineRequest request);

    MedicineResponse getMedicineById(Long id);

    List<MedicineResponse> getAllMedicines();

    MedicineResponse updateStock(Long id, StockUpdateRequest request);

    void deleteMedicine(Long id);

}
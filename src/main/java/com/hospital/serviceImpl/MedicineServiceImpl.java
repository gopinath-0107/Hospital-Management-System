package com.hospital.serviceImpl;

import com.hospital.dto.MedicineRequest;
import com.hospital.dto.MedicineResponse;
import com.hospital.entity.Medicine;
import com.hospital.enums.Status;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.MedicineRepository;
import com.hospital.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hospital.dto.StockUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    @Override
    @Transactional
    public MedicineResponse addMedicine(MedicineRequest request) {

        if (medicineRepository.existsByMedicineName(request.getMedicineName())) {
            throw new DuplicateResourceException(
                    "Medicine with name '" + request.getMedicineName() + "' already exists."
            );
        }

        if (request.getPrice() == null || request.getPrice().doubleValue() <= 0) {
            throw new IllegalArgumentException("Medicine price must be greater than zero.");
        }

        if (request.getStockQuantity() == null || request.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }

        if (request.getExpiryDate() != null
                && request.getManufacturingDate() != null
                && request.getExpiryDate().isBefore(request.getManufacturingDate())) {

            throw new IllegalArgumentException(
                    "Expiry date cannot be before manufacturing date."
            );
        }

        Medicine medicine = Medicine.builder()
                .medicineCode(generateMedicineCode())
                .medicineName(request.getMedicineName())
                .company(request.getCompany())
                .category(request.getCategory())
                .description(request.getDescription())
                .stockQuantity(request.getStockQuantity())
                .price(request.getPrice())
                .batchNumber(request.getBatchNumber())
                .manufacturingDate(request.getManufacturingDate())
                .expiryDate(request.getExpiryDate())
                .status(Status.ACTIVE)
                .build();

        Medicine savedMedicine = medicineRepository.save(medicine);

        return mapToResponse(savedMedicine);
    }


    private String generateMedicineCode() {

        long nextId = medicineRepository.count() + 1;

        return String.format("MED%04d", nextId);
    }

    @Override
    @Transactional
    public MedicineResponse updateMedicine(Long id, MedicineRequest request) {

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicine not found with ID: " + id));

        if (!medicine.getMedicineName().equalsIgnoreCase(request.getMedicineName())
                && medicineRepository.existsByMedicineName(request.getMedicineName())) {

            throw new DuplicateResourceException(
                    "Medicine with name '" + request.getMedicineName() + "' already exists.");
        }

        if (request.getPrice() == null || request.getPrice().doubleValue() <= 0) {
            throw new IllegalArgumentException("Medicine price must be greater than zero.");
        }

        if (request.getStockQuantity() == null || request.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }

        if (request.getManufacturingDate() != null
                && request.getExpiryDate() != null
                && request.getExpiryDate().isBefore(request.getManufacturingDate())) {

            throw new IllegalArgumentException(
                    "Expiry date cannot be before manufacturing date.");
        }

        medicine.setMedicineName(request.getMedicineName());
        medicine.setCompany(request.getCompany());
        medicine.setCategory(request.getCategory());
        medicine.setDescription(request.getDescription());
        medicine.setStockQuantity(request.getStockQuantity());
        medicine.setPrice(request.getPrice());
        medicine.setBatchNumber(request.getBatchNumber());
        medicine.setManufacturingDate(request.getManufacturingDate());
        medicine.setExpiryDate(request.getExpiryDate());

        Medicine updatedMedicine = medicineRepository.save(medicine);

        return mapToResponse(updatedMedicine);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineResponse getMedicineById(Long id) {

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicine not found with ID: " + id));

        return mapToResponse(medicine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponse> getAllMedicines() {

        return medicineRepository.findByStatus(Status.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicineResponse updateStock(Long id, StockUpdateRequest request) {

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicine not found with ID: " + id));

        if (request.getStockQuantity() == null || request.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }

        medicine.setStockQuantity(request.getStockQuantity());

        Medicine updatedMedicine = medicineRepository.save(medicine);

        return mapToResponse(updatedMedicine);
    }

    @Override
    @Transactional
    public void deleteMedicine(Long id) {

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicine not found with ID: " + id));

        if (medicine.getStatus() == Status.INACTIVE) {
            throw new DuplicateResourceException("Medicine is already inactive.");
        }

        medicine.setStatus(Status.INACTIVE);

        medicineRepository.save(medicine);
    }

    private MedicineResponse mapToResponse(Medicine medicine) {

        MedicineResponse response = new MedicineResponse();

        response.setId(medicine.getId());
        response.setMedicineCode(medicine.getMedicineCode());
        response.setMedicineName(medicine.getMedicineName());
        response.setCompany(medicine.getCompany());
        response.setCategory(medicine.getCategory());
        response.setDescription(medicine.getDescription());
        response.setStockQuantity(medicine.getStockQuantity());
        response.setPrice(medicine.getPrice());
        response.setBatchNumber(medicine.getBatchNumber());
        response.setManufacturingDate(medicine.getManufacturingDate());
        response.setExpiryDate(medicine.getExpiryDate());
        response.setStatus(medicine.getStatus());

        return response;
    }
}
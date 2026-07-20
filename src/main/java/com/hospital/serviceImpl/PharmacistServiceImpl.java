package com.hospital.serviceImpl;

import com.hospital.dto.PharmacistRequest;
import com.hospital.dto.PharmacistResponse;
import com.hospital.entity.Pharmacist;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.PharmacistRepository;
import com.hospital.service.PharmacistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacistServiceImpl implements PharmacistService {

    private final PharmacistRepository pharmacistRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public PharmacistResponse createPharmacist(PharmacistRequest request) {

        if (pharmacistRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        if (pharmacistRepository.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Mobile number already exists.");
        }

        if (pharmacistRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new RuntimeException("License number already exists.");
        }

        Pharmacist pharmacist = Pharmacist.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .qualification(request.getQualification())
                .licenseNumber(request.getLicenseNumber())
                .build();

        Pharmacist savedPharmacist = pharmacistRepository.save(pharmacist);

        return mapToResponse(savedPharmacist);
    }

    @Override
    @Transactional
    public PharmacistResponse updatePharmacist(Long id, PharmacistRequest request) {

        Pharmacist pharmacist = pharmacistRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pharmacist not found with ID: " + id));

        pharmacist.setFirstName(request.getFirstName());
        pharmacist.setLastName(request.getLastName());
        pharmacist.setEmail(request.getEmail());
        pharmacist.setMobile(request.getMobile());
        pharmacist.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        pharmacist.setGender(request.getGender());
        pharmacist.setQualification(request.getQualification());
        pharmacist.setLicenseNumber(request.getLicenseNumber());

        Pharmacist updatedPharmacist = pharmacistRepository.save(pharmacist);

        return mapToResponse(updatedPharmacist);
    }

    @Override
    @Transactional(readOnly = true)
    public PharmacistResponse getPharmacistById(Long id) {

        Pharmacist pharmacist = pharmacistRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pharmacist not found with ID: " + id));

        return mapToResponse(pharmacist);
    }


    @Override
    @Transactional(readOnly = true)
    public List<PharmacistResponse> getAllPharmacists() {

        return pharmacistRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    @Transactional
    public void deletePharmacist(Long id) {

        Pharmacist pharmacist = pharmacistRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pharmacist not found with ID: " + id));


        pharmacistRepository.delete(pharmacist);
    }



    private PharmacistResponse mapToResponse(Pharmacist pharmacist) {

        return PharmacistResponse.builder()
                .id(pharmacist.getId())
                .firstName(pharmacist.getFirstName())
                .lastName(pharmacist.getLastName())
                .email(pharmacist.getEmail())
                .mobile(pharmacist.getMobile())
                .qualification(pharmacist.getQualification())
                .licenseNumber(pharmacist.getLicenseNumber())
                .build();
    }

}
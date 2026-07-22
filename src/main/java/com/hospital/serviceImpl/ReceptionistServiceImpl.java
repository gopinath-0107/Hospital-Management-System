package com.hospital.serviceImpl;

import com.hospital.dto.CreateReceptionistRequest;
import com.hospital.dto.ReceptionistResponse;
import com.hospital.entity.Receptionist;
import com.hospital.enums.Role;
import com.hospital.enums.Status;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.ReceptionistRepository;
import com.hospital.service.ReceptionistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceptionistServiceImpl implements ReceptionistService {

    private final ReceptionistRepository receptionistRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ReceptionistResponse createReceptionist(CreateReceptionistRequest request) {

        if (receptionistRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (receptionistRepository.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Mobile already exists");
        }

        Receptionist receptionist = Receptionist.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())

                // BCrypt Password
                .password(passwordEncoder.encode(request.getPassword()))

                .gender(request.getGender())
                .shift(request.getShift())
                .role(Role.RECEPTIONIST)
                .status(Status.ACTIVE)
                .build();

        Receptionist savedReceptionist = receptionistRepository.save(receptionist);

        return mapToResponse(savedReceptionist);
    }

    @Override
    public ReceptionistResponse getReceptionistById(Long id) {

        Receptionist receptionist = receptionistRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receptionist not found"));

        return mapToResponse(receptionist);
    }

    @Override
    public List<ReceptionistResponse> getAllReceptionists() {

        return receptionistRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReceptionistResponse updateReceptionist(Long id,
                                                   CreateReceptionistRequest request) {

        Receptionist receptionist = receptionistRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receptionist not found"));

        receptionist.setFirstName(request.getFirstName());
        receptionist.setLastName(request.getLastName());
        receptionist.setMobile(request.getMobile());
        receptionist.setGender(request.getGender());
        receptionist.setShift(request.getShift());



        Receptionist updatedReceptionist = receptionistRepository.save(receptionist);

        return mapToResponse(updatedReceptionist);
    }

    @Override
    @Transactional
    public void deleteReceptionist(Long id) {

        Receptionist receptionist = receptionistRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receptionist not found"));

        receptionist.setStatus(Status.INACTIVE);

        receptionistRepository.save(receptionist);
    }

    private ReceptionistResponse mapToResponse(Receptionist receptionist) {

        return ReceptionistResponse.builder()
                .id(receptionist.getId())
                .firstName(receptionist.getFirstName())
                .lastName(receptionist.getLastName())
                .email(receptionist.getEmail())
                .mobile(receptionist.getMobile())
                .gender(receptionist.getGender())
                .shift(receptionist.getShift())
                .status(receptionist.getStatus())
                .createdAt(receptionist.getCreatedAt())
                .updatedAt(receptionist.getUpdatedAt())
                .build();
    }
}
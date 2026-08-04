package com.hospital.serviceImpl;

import com.hospital.dto.WardRequest;
import com.hospital.dto.WardResponse;
import com.hospital.entity.Ward;
import com.hospital.enums.WardStatus;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.WardRepository;
import com.hospital.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WardServiceImpl implements WardService {

    private final WardRepository wardRepository;

    @Override
    public WardResponse createWard(WardRequest request) {

        if (wardRepository.existsByWardNameIgnoreCase(request.getWardName().trim())) {
            throw new BadRequestException("Ward already exists with name: " + request.getWardName());
        }

        Ward ward = new Ward();
        ward.setWardName(request.getWardName().trim());
        ward.setWardType(request.getWardType());
        ward.setFloor(request.getFloor());
        ward.setTotalRooms(request.getTotalRooms());
        ward.setDescription(request.getDescription());
        ward.setStatus(request.getStatus());

        Ward savedWard = wardRepository.save(ward);

        return mapToResponse(savedWard);
    }

    @Override
    public WardResponse updateWard(Long id, WardRequest request) {

        Ward ward = wardRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ward not found with ID: " + id));

        if (!ward.getWardName().equalsIgnoreCase(request.getWardName().trim())
                && wardRepository.existsByWardNameIgnoreCase(request.getWardName().trim())) {

            throw new BadRequestException("Ward already exists with name: " + request.getWardName());
        }

        ward.setWardName(request.getWardName().trim());
        ward.setWardType(request.getWardType());
        ward.setFloor(request.getFloor());
        ward.setTotalRooms(request.getTotalRooms());
        ward.setDescription(request.getDescription());
        ward.setStatus(request.getStatus());

        Ward updatedWard = wardRepository.save(ward);

        return mapToResponse(updatedWard);
    }

    @Override
    @Transactional(readOnly = true)
    public WardResponse getWardById(Long id) {

        Ward ward = wardRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ward not found with ID: " + id));

        return mapToResponse(ward);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WardResponse> getAllWards() {

        return wardRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WardResponse> getActiveWards() {

        return wardRepository.findByStatus(WardStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteWard(Long id) {

        Ward ward = wardRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ward not found with ID: " + id));

        wardRepository.delete(ward);
    }

    private WardResponse mapToResponse(Ward ward) {

        return WardResponse.builder()
                .id(ward.getId())
                .wardName(ward.getWardName())
                .wardType(ward.getWardType())
                .floor(ward.getFloor())
                .totalRooms(ward.getTotalRooms())
                .description(ward.getDescription())
                .status(ward.getStatus())
                .createdAt(ward.getCreatedAt())
                .updatedAt(ward.getUpdatedAt())
                .build();
    }
}
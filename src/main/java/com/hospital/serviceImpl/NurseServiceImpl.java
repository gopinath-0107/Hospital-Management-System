package com.hospital.serviceImpl;

import com.hospital.dto.CreateNurseRequest;
import com.hospital.dto.NurseResponse;
import com.hospital.entity.Department;
import com.hospital.entity.Nurse;
import com.hospital.enums.Role;
import com.hospital.enums.Status;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.DepartmentRepository;
import com.hospital.repo.NurseRepository;
import com.hospital.service.NurseService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NurseServiceImpl implements NurseService {


    private final NurseRepository nurseRepository;

    private final DepartmentRepository departmentRepository;


    @Override
    @Transactional
    public NurseResponse createNurse(CreateNurseRequest request) {


        if(nurseRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }


        if(nurseRepository.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Mobile already exists");
        }


        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));


        Nurse nurse = Nurse.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .password(request.getPassword())
                .gender(request.getGender())
                .qualification(request.getQualification())
                .experience(request.getExperience())
                .department(department)
                .shift(request.getShift())
                .role(Role.NURSE)
                .build();


        Nurse savedNurse = nurseRepository.save(nurse);


        return mapToResponse(savedNurse);
    }



    @Override
    public NurseResponse getNurseById(Long id) {

        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Nurse not found"));

        return mapToResponse(nurse);
    }



    @Override
    public List<NurseResponse> getAllNurses() {

        return nurseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }



    @Override
    @Transactional
    public NurseResponse updateNurse(Long id, CreateNurseRequest request) {


        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Nurse not found"));


        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));



        nurse.setFirstName(request.getFirstName());
        nurse.setLastName(request.getLastName());
        nurse.setMobile(request.getMobile());
        nurse.setGender(request.getGender());
        nurse.setQualification(request.getQualification());
        nurse.setExperience(request.getExperience());
        nurse.setDepartment(department);
        nurse.setShift(request.getShift());


        return mapToResponse(nurseRepository.save(nurse));
    }



    @Override
    @Transactional
    public void deleteNurse(Long id) {

        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Nurse not found"));


        nurse.setStatus(Status.INACTIVE);

        nurseRepository.save(nurse);
    }



    private NurseResponse mapToResponse(Nurse nurse) {


        return NurseResponse.builder()
                .id(nurse.getId())
                .firstName(nurse.getFirstName())
                .lastName(nurse.getLastName())
                .email(nurse.getEmail())
                .mobile(nurse.getMobile())
                .gender(nurse.getGender())
                .qualification(nurse.getQualification())
                .experience(nurse.getExperience())
                .departmentId(nurse.getDepartment().getId())
                .departmentName(nurse.getDepartment().getDepartmentName())
                .shift(nurse.getShift())
                .status(nurse.getStatus())
                .createdAt(nurse.getCreatedAt())
                .updatedAt(nurse.getUpdatedAt())
                .build();
    }
}
package com.hospital.serviceImpl;

import com.hospital.dto.CreateLabTechnicianRequest;
import com.hospital.dto.LabTechnicianResponse;
import com.hospital.entity.Department;
import com.hospital.entity.LabTechnician;
import com.hospital.enums.Role;
import com.hospital.enums.Status;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.DepartmentRepository;
import com.hospital.repo.LabTechnicianRepository;
import com.hospital.service.LabTechnicianService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LabTechnicianServiceImpl implements LabTechnicianService {


    private final LabTechnicianRepository labTechnicianRepository;

    private final DepartmentRepository departmentRepository;



    @Override
    @Transactional
    public LabTechnicianResponse createLabTechnician(
            CreateLabTechnicianRequest request) {


        if(labTechnicianRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException("Email already exists");
        }


        if(labTechnicianRepository.existsByMobile(request.getMobile())) {

            throw new RuntimeException("Mobile already exists");
        }


        if(labTechnicianRepository
                .existsByCertificateNumber(request.getCertificateNumber())) {

            throw new RuntimeException(
                    "Certificate number already exists"
            );
        }



        Department department =
                departmentRepository.findById(request.getDepartmentId())

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found"));



        LabTechnician technician = LabTechnician.builder()

                .firstName(request.getFirstName())

                .lastName(request.getLastName())

                .email(request.getEmail())

                .mobile(request.getMobile())

                .password(request.getPassword())

                .gender(request.getGender())

                .qualification(request.getQualification())

                .experience(request.getExperience())

                .certificateNumber(
                        request.getCertificateNumber()
                )

                .department(department)

                .shift(request.getShift())

                .role(Role.LAB_TECHNICIAN)

                .build();



        LabTechnician savedTechnician =
                labTechnicianRepository.save(technician);



        return mapToResponse(savedTechnician);
    }





    @Override
    public LabTechnicianResponse getLabTechnicianById(Long id) {


        LabTechnician technician =
                labTechnicianRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab Technician not found"));



        return mapToResponse(technician);
    }





    @Override
    public List<LabTechnicianResponse> getAllLabTechnicians() {


        return labTechnicianRepository.findAll()

                .stream()

                .map(this::mapToResponse)

                .collect(Collectors.toList());
    }





    @Override
    @Transactional
    public LabTechnicianResponse updateLabTechnician(
            Long id,
            CreateLabTechnicianRequest request) {


        LabTechnician technician =
                labTechnicianRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab Technician not found"));



        Department department =
                departmentRepository.findById(request.getDepartmentId())

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found"));



        technician.setFirstName(request.getFirstName());

        technician.setLastName(request.getLastName());

        technician.setMobile(request.getMobile());

        technician.setGender(request.getGender());

        technician.setQualification(
                request.getQualification()
        );

        technician.setExperience(
                request.getExperience()
        );

        technician.setCertificateNumber(
                request.getCertificateNumber()
        );

        technician.setDepartment(department);

        technician.setShift(request.getShift());



        return mapToResponse(
                labTechnicianRepository.save(technician)
        );
    }





    @Override
    @Transactional
    public void deleteLabTechnician(Long id) {


        LabTechnician technician =
                labTechnicianRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab Technician not found"));



        technician.setStatus(Status.INACTIVE);


        labTechnicianRepository.save(technician);
    }





    private LabTechnicianResponse mapToResponse(
            LabTechnician technician) {


        return LabTechnicianResponse.builder()

                .id(technician.getId())

                .firstName(technician.getFirstName())

                .lastName(technician.getLastName())

                .email(technician.getEmail())

                .mobile(technician.getMobile())

                .gender(technician.getGender())

                .qualification(
                        technician.getQualification()
                )

                .experience(
                        technician.getExperience()
                )

                .certificateNumber(
                        technician.getCertificateNumber()
                )

                .departmentId(
                        technician.getDepartment().getId()
                )

                .departmentName(
                        technician.getDepartment()
                                .getDepartmentName()
                )

                .shift(technician.getShift())

                .status(technician.getStatus())

                .createdAt(technician.getCreatedAt())

                .updatedAt(technician.getUpdatedAt())

                .build();
    }
}
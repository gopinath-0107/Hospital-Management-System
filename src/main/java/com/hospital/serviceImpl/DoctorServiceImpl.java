package com.hospital.serviceImpl;

import com.hospital.dto.DoctorResponse;
import com.hospital.entity.Doctor;
import com.hospital.entity.DoctorAvailability;
import com.hospital.enums.AvailabilityStatus;
import com.hospital.enums.Status;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.DoctorAvailabilityRepository;
import com.hospital.repo.DoctorRepository;
import com.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorProfile(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with ID : " + id
                        )
                );

        return mapToResponse(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctors(
            Long departmentId,
            Long specializationId
    ) {

        List<Doctor> doctors =
                doctorRepository.findByDepartmentIdAndSpecializationIdAndAvailableAndStatus(
                        departmentId,
                        specializationId,
                        true,
                        Status.ACTIVE
                );

        return doctors.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private DoctorResponse mapToResponse(Doctor doctor) {

        return DoctorResponse.builder()

                .id(doctor.getId())

                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())

                .email(doctor.getEmail())
                .mobile(doctor.getMobile())

                .departmentId(doctor.getDepartment().getId())
                .departmentName(doctor.getDepartment().getDepartmentName())

                .specializationId(doctor.getSpecialization().getId())
                .specializationName(doctor.getSpecialization().getSpecializationName())

                .qualification(doctor.getQualification())
                .experience(doctor.getExperience())

                .consultationFee(doctor.getConsultationFee())

                .licenseNumber(doctor.getLicenseNumber())

                .available(doctor.getAvailable())

                .profileImage(doctor.getProfileImage())

                .role(doctor.getRole())

                .status(doctor.getStatus())

                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors() {

        return doctorRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAvailableDoctors(
            LocalDate date,
            Long departmentId,
            Long specializationId) {

        List<DoctorAvailability> availabilityList =
                doctorAvailabilityRepository
                        .findByAvailableDateAndStatusAndDoctorDepartmentIdAndDoctorSpecializationId(
                                date,
                                AvailabilityStatus.AVAILABLE,
                                departmentId,
                                specializationId
                        );

        return availabilityList.stream()
                .map(DoctorAvailability::getDoctor)
                .distinct()
                .map(this::mapToResponse)
                .toList();
    }
}
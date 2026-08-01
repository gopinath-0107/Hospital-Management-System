package com.hospital.serviceImpl;

import com.hospital.dto.DoctorResponse;
import com.hospital.dto.request.DoctorRegistrationRequest;
import com.hospital.dto.request.LoginRequest;
import com.hospital.dto.request.PatientRegistrationRequest;
import com.hospital.dto.response.*;
import com.hospital.entity.*;
import com.hospital.enums.Role;
import com.hospital.enums.Status;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.exception.UnauthorizedException;
import com.hospital.repo.*;
import com.hospital.security.CustomUserDetails;
import com.hospital.security.JwtService;
import com.hospital.service.AuthService;
import com.hospital.service.ValidationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final NurseRepository nurseRepository;
    private final ReceptionistRepository receptionistRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final PharmacistRepository pharmacistRepository;
    private final SpecializationRepository specializationRepository;

    @Override
    public AuthResponse login(LoginRequest request) {


        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);

            return AuthResponse.builder()
                    .token(jwt)
                    .role(Role.valueOf(userDetails.getRole()))
                    .userId(userDetails.getId())
                    .name(userDetails.getName())
                    .username(userDetails.getUsername())
                    .build();
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new UnauthorizedException("Invalid email or password : " + e.getMessage());
        }
    }

    @Override
    @PostConstruct
    public void createDefaultAdmin() {
        String adminEmail = "admin@hospital.com";
        if (!adminRepository.existsByEmail(adminEmail)) {
            Admin admin = Admin.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .build();
            adminRepository.save(admin);
            System.out.println("Default Admin created successfully!");
        }
    }

    @Override
    public ApiResponse<DoctorResponse> registerDoctor(DoctorRegistrationRequest request) {

        validationService.validateEmailAndMobile(
                request.getEmail(),
                request.getMobile()
        );

        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new DuplicateResourceException("License number already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        Specialization specialization = specializationRepository
                .findById(request.getSpecializationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Specialization not found"));

        Doctor doctor = Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .department(department)
                .specialization(specialization)
                .qualification(request.getQualification())
                .experience(request.getExperience())
                .consultationFee(request.getConsultationFee())
                .licenseNumber(request.getLicenseNumber())
                .role(Role.DOCTOR)
                .available(true)
                .status(Status.ACTIVE)
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);

        DoctorResponse response = DoctorResponse.builder()
                .id(savedDoctor.getId())
                .firstName(savedDoctor.getFirstName())
                .lastName(savedDoctor.getLastName())
                .email(savedDoctor.getEmail())
                .mobile(savedDoctor.getMobile())
                .gender(savedDoctor.getGender())

                .departmentId(savedDoctor.getDepartment().getId())
                .departmentName(savedDoctor.getDepartment().getDepartmentName())

                .specializationId(savedDoctor.getSpecialization().getId())
                .specializationName(savedDoctor.getSpecialization().getSpecializationName())

                .qualification(savedDoctor.getQualification())
                .experience(savedDoctor.getExperience())
                .consultationFee(savedDoctor.getConsultationFee())
                .licenseNumber(savedDoctor.getLicenseNumber())
                .role(savedDoctor.getRole())
                .available(savedDoctor.getAvailable())
                .status(savedDoctor.getStatus())
                .build();

        return ApiResponse.<DoctorResponse>builder()
                .message("Doctor registered successfully")
                .status(201)
                .data(response)
                .build();
    }
    @Override
    public ApiResponse<PatientResponse> registerPatient(PatientRegistrationRequest request) {

        validationService.validateEmailAndMobile(
                request.getEmail(),
                request.getMobile()
        );

        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .bloodGroup(request.getBloodGroup())
                .height(request.getHeight())
                .weight(request.getWeight())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .emergencyContact(request.getEmergencyContact())
                .role(Role.PATIENT)
                .status(Status.ACTIVE)
                .build();

        Patient savedPatient = patientRepository.save(patient);

        PatientResponse response = PatientResponse.builder()
                .id(savedPatient.getId())
                .firstName(savedPatient.getFirstName())
                .lastName(savedPatient.getLastName())
                .email(savedPatient.getEmail())
                .mobile(savedPatient.getMobile())
                .gender(savedPatient.getGender())
                .dateOfBirth(savedPatient.getDateOfBirth())
                .bloodGroup(savedPatient.getBloodGroup())
                .height(savedPatient.getHeight())
                .weight(savedPatient.getWeight())
                .address(savedPatient.getAddress())
                .city(savedPatient.getCity())
                .state(savedPatient.getState())
                .pincode(savedPatient.getPincode())
                .emergencyContact(savedPatient.getEmergencyContact())
                .role(savedPatient.getRole())
                .status(savedPatient.getStatus())
                .build();

        return ApiResponse.<PatientResponse>builder()
                .message("Patient registered successfully")
                .status(201)
                .data(response)
                .build();
    }

}

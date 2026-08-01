package com.hospital.security;

import com.hospital.entity.*;
import com.hospital.enums.Role;
import com.hospital.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final NurseRepository nurseRepository;
    private final ReceptionistRepository receptionistRepository;
    private final PharmacistRepository pharmacistRepository;

    private final LabTechnicianRepository labTechnicianRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByEmail(username);
        if (admin.isPresent()) {
            Admin a = admin.get();
            return new CustomUserDetails(a.getEmail(), a.getPassword(), Role.ADMIN.name(), a.getId(), a.getEmail());
        }

        Optional<Doctor> doctor = doctorRepository.findByEmailOrMobile(
                username,
                username
        );
        if (doctor.isPresent()) {
            Doctor d = doctor.get();
            return new CustomUserDetails(d.getEmail(), d.getPassword(), Role.DOCTOR.name(), d.getId(), d.getFirstName() + " " + d.getLastName());
        }

        Optional<Patient> patient = patientRepository.findByEmailOrMobile(
                username,
                username
        );
        if (patient.isPresent()) {
            Patient p = patient.get();
            return new CustomUserDetails(p.getEmail(), p.getPassword(), Role.PATIENT.name(), p.getId(), p.getFirstName() + " " + p.getLastName());
        }

        Optional<Nurse> nurse = nurseRepository.findByEmailOrMobile(
                username,
                username
        );
        if (nurse.isPresent()) {
            Nurse n = nurse.get();
            return new CustomUserDetails(n.getEmail(), n.getPassword(), Role.NURSE.name(), n.getId(), n.getFirstName() + " " + n.getLastName());
        }


        Optional<LabTechnician> labTechnician = labTechnicianRepository.findByEmailOrMobile(
                username,
                username
        );
        if (labTechnician.isPresent()) {
            LabTechnician n = labTechnician.get();
            return new CustomUserDetails(n.getEmail(), n.getPassword(), Role.LAB_TECHNICIAN.name(), n.getId(), n.getFirstName() + " " + n.getLastName());
        }


        Optional<Pharmacist> pharmacist = pharmacistRepository.findByEmailOrMobile(
                username,
                username
        );


        if (pharmacist.isPresent()) {

            Pharmacist p = pharmacist.get();

            return new CustomUserDetails(
                    p.getEmail(),
                    p.getPassword(),
                    Role.PHARMACIST.name(),
                    p.getId(),
                    p.getFirstName() + " " + p.getLastName()

            );



        }


        Optional <Receptionist> receptionist = receptionistRepository.findByEmailOrMobile(
                username,
                username
        );

        if (receptionist.isPresent()) {
            Receptionist n = receptionist.get();
            return new CustomUserDetails(n.getEmail(), n.getPassword(), Role.RECEPTIONIST.name(), n.getId(), n.getFirstName() + " " + n.getLastName());
        }




        throw new UsernameNotFoundException(
                "User not found with Email or Mobile."
        );
    }
}

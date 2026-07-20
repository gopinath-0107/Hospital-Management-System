package com.hospital.security;

import com.hospital.entity.Admin;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.entity.Nurse;
import com.hospital.entity.Receptionist;
import com.hospital.enums.Role;
import com.hospital.repo.AdminRepository;
import com.hospital.repo.DoctorRepository;
import com.hospital.repo.PatientRepository;
import com.hospital.repo.NurseRepository;
import com.hospital.repo.ReceptionistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import com.hospital.entity.Pharmacist;
import com.hospital.repo.PharmacistRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final NurseRepository nurseRepository;
    private final ReceptionistRepository receptionistRepository;
    private final PharmacistRepository pharmacistRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            Admin a = admin.get();
            return new CustomUserDetails(a.getEmail(), a.getPassword(), Role.ADMIN.name(), a.getId(), a.getEmail());
        }

        Optional<Doctor> doctor = doctorRepository.findByEmail(email);
        if (doctor.isPresent()) {
            Doctor d = doctor.get();
            return new CustomUserDetails(d.getEmail(), d.getPassword(), Role.DOCTOR.name(), d.getId(), d.getFirstName() + " " + d.getLastName());
        }

        Optional<Patient> patient = patientRepository.findByEmail(email);
        if (patient.isPresent()) {
            Patient p = patient.get();
            return new CustomUserDetails(p.getEmail(), p.getPassword(), Role.PATIENT.name(), p.getId(), p.getFirstName() + " " + p.getLastName());
        }

        Optional<Nurse> nurse = nurseRepository.findByEmail(email);
        if (nurse.isPresent()) {
            Nurse n = nurse.get();
            return new CustomUserDetails(n.getEmail(), n.getPassword(), Role.NURSE.name(), n.getId(), n.getFirstName() + " " + n.getLastName());
        }


        Optional<Pharmacist> pharmacist = pharmacistRepository.findByEmail(email);


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




        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}

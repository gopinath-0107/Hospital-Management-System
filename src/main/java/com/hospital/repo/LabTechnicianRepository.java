package com.hospital.repo;


import com.hospital.entity.LabTechnician;

import com.hospital.entity.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LabTechnicianRepository
        extends JpaRepository<LabTechnician, Long> {

    Optional<LabTechnician> findByEmail(String email);



    boolean existsByEmail(String email);


    boolean existsByMobile(String mobile);


    boolean existsByCertificateNumber(String certificateNumber);

}
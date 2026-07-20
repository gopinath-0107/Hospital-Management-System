package com.hospital.repo;


import com.hospital.entity.LabTechnician;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LabTechnicianRepository
        extends JpaRepository<LabTechnician, Long> {


    boolean existsByEmail(String email);


    boolean existsByMobile(String mobile);


    boolean existsByCertificateNumber(String certificateNumber);

}
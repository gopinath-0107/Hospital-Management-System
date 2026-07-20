package com.hospital.repo;


import com.hospital.entity.Receptionist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReceptionistRepository
        extends JpaRepository<Receptionist, Long> {


    boolean existsByEmail(String email);


    boolean existsByMobile(String mobile);
}
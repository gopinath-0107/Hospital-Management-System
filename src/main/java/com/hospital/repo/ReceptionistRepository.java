package com.hospital.repo;


import com.hospital.entity.Pharmacist;
import com.hospital.entity.Receptionist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ReceptionistRepository
        extends JpaRepository<Receptionist, Long> {

    Optional<Receptionist> findByEmail(String email);

    Optional<Receptionist> findByEmailOrMobile(
            String email,
            String mobile
    );

    boolean existsByEmail(String email);


    boolean existsByMobile(String mobile);
}
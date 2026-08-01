package com.hospital.repo;

import com.hospital.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {

    Optional<Nurse> findByEmail(String email);

    Optional<Nurse> findByEmailOrMobile(
            String email,
            String mobile
    );

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);
}
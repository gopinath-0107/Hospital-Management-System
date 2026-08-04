package com.hospital.repo;

import com.hospital.entity.Ward;
import com.hospital.enums.WardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {

    /**
     * Check if Ward Name already exists
     */
    boolean existsByWardNameIgnoreCase(String wardName);

    /**
     * Find Ward by Name
     */
    Optional<Ward> findByWardNameIgnoreCase(String wardName);

    /**
     * Get all Active / Inactive Wards
     */
    List<Ward> findByStatus(WardStatus status);

}
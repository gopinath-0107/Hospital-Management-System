package com.hospital.repo;

import com.hospital.entity.Specialization;
import com.hospital.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    Optional<Specialization> findById(Long id);

    List<Specialization> findByDepartmentId(Long departmentId);

    List<Specialization> findByStatus(Status status);

    List<Specialization> findByDepartmentIdAndStatus(
            Long departmentId,
            Status status
    );

    boolean existsByDepartmentIdAndSpecializationName(
            Long departmentId,
            String specializationName
    );

    Optional<Specialization> findByIdAndStatus(
            Long id,
            Status status
    );

}
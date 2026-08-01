package com.hospital.repo;

import com.hospital.entity.Department;
import com.hospital.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByDepartmentName(String departmentName);
    List<Department> findByStatus(Status status);
    boolean existsByDepartmentCode(String departmentCode);

    Optional<Department> findByDepartmentCode(String departmentCode);

}

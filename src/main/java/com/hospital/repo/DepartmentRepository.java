package com.hospital.repo;

import com.hospital.entity.Department;
import com.hospital.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByDepartmentName(String departmentName);
    List<Department> findByStatus(Status status);

}

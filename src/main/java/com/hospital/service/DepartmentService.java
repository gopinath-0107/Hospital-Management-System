package com.hospital.service;

import com.hospital.dto.DepartmentRequest;
import com.hospital.dto.DepartmentResponse;
import com.hospital.dto.response.ApiResponse;
import com.hospital.entity.Department;
import com.hospital.enums.Status;
import com.hospital.repo.DepartmentRepository;

import java.util.List;
public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest request);

    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);

    ApiResponse<String> deleteDepartment(Long id);

    DepartmentResponse getDepartmentById(Long id);

    List<DepartmentResponse> getAllDepartments();
}

package com.hospital.serviceImpl;

import com.hospital.dto.DepartmentRequest;
import com.hospital.dto.DepartmentResponse;
import com.hospital.dto.response.ApiResponse;
import com.hospital.entity.Department;
import com.hospital.enums.Status;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.DepartmentRepository;
import com.hospital.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public void importDepartments(MultipartFile file) {

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                DepartmentRequest request = new DepartmentRequest();

                request.setDepartmentCode(
                        row.getCell(0).getStringCellValue().trim());

                request.setDepartmentName(
                        row.getCell(1).getStringCellValue().trim());

                request.setDescription(
                        row.getCell(2).getStringCellValue().trim());

                request.setFloorNumber(
                        (int) row.getCell(3).getNumericCellValue());

                request.setStatus(
                        Status.valueOf(
                                row.getCell(4)
                                        .getStringCellValue()
                                        .trim()
                                        .toUpperCase()
                        )
                );

                createDepartment(request);

            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to import departments.", e);
        }
    }

    @Override
    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByDepartmentName(request.getDepartmentName())) {
            throw new DuplicateResourceException("Department with name '" + request.getDepartmentName() + "' already exists.");
        }

        Department department = Department.builder()
                .departmentName(request.getDepartmentName())
                .departmentCode(request.getDepartmentCode())
                .description(request.getDescription())
                .floorNumber(request.getFloorNumber())
                .status(request.getStatus())
                .build();

        Department saved = departmentRepository.save(department);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        if (!department.getDepartmentName().equalsIgnoreCase(request.getDepartmentName()) &&
                departmentRepository.existsByDepartmentName(request.getDepartmentName())) {
            throw new DuplicateResourceException("Department with name '" + request.getDepartmentName() + "' already exists.");
        }

        department.setDepartmentName(request.getDepartmentName());
        department.setDescription(request.getDescription());
        department.setFloorNumber(request.getFloorNumber());
        department.setStatus(request.getStatus());

        Department updated = departmentRepository.save(department);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found with ID: " + id));

        if (department.getStatus() == Status.INACTIVE) {
            throw new DuplicateResourceException("Department is already inactive");
        }

        department.setStatus(Status.INACTIVE);
        departmentRepository.save(department);

        return ApiResponse.<String>builder()
                .status(200)
                .message("Department deactivated successfully")
                .data(null)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        return mapToResponse(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {

        return departmentRepository.findByStatus(Status.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private DepartmentResponse mapToResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .departmentName(department.getDepartmentName())
                .description(department.getDescription())
                .floorNumber(department.getFloorNumber())
                .status(department.getStatus())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
}

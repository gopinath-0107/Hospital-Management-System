package com.hospital.dto;

import com.hospital.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String departmentName;
    private String description;
    private Integer floorNumber;
    private Status status;
    private String departmentCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

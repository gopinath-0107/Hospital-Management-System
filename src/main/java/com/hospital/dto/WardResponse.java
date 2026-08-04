package com.hospital.dto;

import com.hospital.enums.WardStatus;
import com.hospital.enums.WardType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WardResponse {

    private Long id;

    private String wardName;

    private WardType wardType;

    private Integer floor;

    private Integer totalRooms;

    private String description;

    private WardStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
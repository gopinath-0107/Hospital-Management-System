package com.hospital.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyDashboardResponse {

    private Long totalMedicines;

    private Long lowStockMedicines;

    private Long totalDispensed;

}
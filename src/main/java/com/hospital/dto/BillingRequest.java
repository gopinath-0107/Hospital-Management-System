package com.hospital.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingRequest {

    private Long patientId;

    private Long appointmentId;

    private BigDecimal consultationFee;

    private BigDecimal medicineAmount;

    private BigDecimal labAmount;

    private BigDecimal discount;

}
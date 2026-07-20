package com.hospital.dto;

import com.hospital.enums.BillingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingResponse {

    private Long billingId;

    private Long patientId;

    private Long appointmentId;

    private BigDecimal consultationFee;

    private BigDecimal medicineAmount;

    private BigDecimal labAmount;

    private BigDecimal discount;

    private BigDecimal totalAmount;

    private BillingStatus billingStatus;

    private LocalDateTime createdAt;

}
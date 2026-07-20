package com.hospital.dto;

import com.hospital.enums.PaymentMode;
import com.hospital.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long paymentId;

    private Long billingId;

    private BigDecimal amount;

    private PaymentMode paymentMode;

    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;

}
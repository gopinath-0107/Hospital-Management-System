package com.hospital.dto;

import com.hospital.enums.PaymentMode;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    private BigDecimal amount;

    private PaymentMode paymentMode;

}
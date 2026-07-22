package com.hospital.dto;

import com.hospital.enums.PaymentMode;
import com.hospital.enums.PaymentMode;
import com.hospital.enums.PaymentStatus;
import com.hospital.enums.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {

    private Long paymentId;

    private BigDecimal amount;

    private PaymentMode paymentMode;

    private PaymentStatus paymentStatus;

    private PaymentType paymentType;

    private String transactionId;

    private String receiptNumber;

    private LocalDateTime paymentDate;

}
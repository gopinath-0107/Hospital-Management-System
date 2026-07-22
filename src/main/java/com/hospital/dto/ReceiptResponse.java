package com.hospital.dto;

import com.hospital.enums.PaymentMode;
import com.hospital.enums.PaymentStatus;
import com.hospital.enums.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptResponse {

    private String receiptNumber;

    private String transactionId;

    private BigDecimal amount;

    private PaymentMode paymentMode;

    private PaymentType paymentType;

    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;

}
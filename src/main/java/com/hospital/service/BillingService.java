package com.hospital.service;

import com.hospital.dto.*;

import java.util.List;

public interface BillingService {

    BillingResponse generateBill(BillingRequest request);

    BillingResponse getBillById(Long billingId);

    List<BillingResponse> getAllBills();

    PaymentResponse makePayment(Long billingId, PaymentRequest request);

    ReceiptResponse getReceipt(Long billingId);

}
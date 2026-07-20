package com.hospital.service;

import com.hospital.dto.CreateLabOrderRequest;
import com.hospital.dto.LabOrderResponse;
import com.hospital.enums.LabOrderStatus;

import java.util.List;

public interface LabOrderService {

    LabOrderResponse createLabOrder(CreateLabOrderRequest request);

    LabOrderResponse getLabOrderById(Long id);

    List<LabOrderResponse> getOrdersByAppointment(Long appointmentId);

    List<LabOrderResponse> getOrdersByDoctor(Long doctorId);

    List<LabOrderResponse> getOrdersByPatient(Long patientId);

    List<LabOrderResponse> getOrdersByStatus(LabOrderStatus status);

    LabOrderResponse updateStatus(Long id, LabOrderStatus status);

}
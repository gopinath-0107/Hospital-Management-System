package com.hospital.serviceImpl;

import com.hospital.dto.CreateLabOrderRequest;
import com.hospital.dto.LabOrderResponse;
import com.hospital.entity.*;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.LabOrderStatus;
import com.hospital.enums.NotificationType;
import com.hospital.enums.Role;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.*;
import com.hospital.service.HospitalNotificationService;
import com.hospital.service.LabOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LabOrderServiceImpl implements LabOrderService {

    private final LabOrderRepository labOrderRepository;
    private final AppointmentRepository appointmentRepository;
    private final LabTestRepository labTestRepository;
    private final ConsultationRepository consultationRepository;
    private final HospitalNotificationService hospitalNotificationService;
    private final LabTechnicianRepository labTechnicianRepository;

    @Override
    @Transactional
    public LabOrderResponse createLabOrder(CreateLabOrderRequest request) {

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with ID : " + request.getAppointmentId()
                        ));

        // Consultation must be completed first
        if (appointment.getStatus() != AppointmentStatus.CONSULTATION_DONE) {
            throw new BadRequestException(
                    "Lab order can only be created after consultation is completed."
            );
        }

        // Consultation must exist
        Consultation consultation = consultationRepository
                .findByAppointmentId(appointment.getId());

        if (consultation == null) {
            throw new BadRequestException(
                    "Consultation not found for this appointment."
            );
        }

        // Duplicate Lab Order Check
        if (labOrderRepository.existsByAppointmentIdAndLabTestId(
                request.getAppointmentId(),
                request.getLabTestId())) {

            throw new DuplicateResourceException(
                    "Lab Order already exists for this test."
            );
        }

        // Lab Test
        LabTest labTest = labTestRepository.findById(request.getLabTestId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab Test not found with ID : " + request.getLabTestId()
                        ));

        if (!labTest.getActive()) {
            throw new BadRequestException(
                    "Selected Lab Test is inactive."
            );
        }

        LabOrder labOrder = LabOrder.builder()
                .appointment(appointment)
                .labTest(labTest)
                .clinicalNotes(request.getClinicalNotes())
                .priority(request.getPriority())
                .instructions(request.getInstructions())
                .status(LabOrderStatus.PENDING)
                .build();

        LabOrder saved = labOrderRepository.save(labOrder);

        // ==========================
// Notification to Lab Technicians
// ==========================

        List<LabTechnician> technicians =
                labTechnicianRepository.findAll();

        for (LabTechnician technician : technicians) {

            hospitalNotificationService.createNotification(
                    technician.getId(),
                    Role.LAB_TECHNICIAN,
                    NotificationType.LAB_ORDER,
                    "New Lab Order",
                    "A new lab order has been assigned for "
                            + appointment.getPatient().getFirstName()
                            + " "
                            + appointment.getPatient().getLastName()
                            + ". Test : "
                            + labTest.getTestName()
            );
        }

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LabOrderResponse getLabOrderById(Long id) {

        LabOrder labOrder = labOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab Order not found with ID : " + id));

        return mapToResponse(labOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabOrderResponse> getOrdersByAppointment(Long appointmentId) {

        List<LabOrder> orders = labOrderRepository.findByAppointmentId(appointmentId);

        List<LabOrderResponse> response = new ArrayList<>();

        for (LabOrder order : orders) {
            response.add(mapToResponse(order));
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabOrderResponse> getOrdersByDoctor(Long doctorId) {

        List<LabOrder> orders =
                labOrderRepository.findByAppointmentDoctorId(doctorId);

        List<LabOrderResponse> response = new ArrayList<>();

        for (LabOrder order : orders) {
            response.add(mapToResponse(order));
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabOrderResponse> getOrdersByPatient(Long patientId) {

        List<LabOrder> orders =
                labOrderRepository.findByAppointmentPatientId(patientId);

        List<LabOrderResponse> response = new ArrayList<>();

        for (LabOrder order : orders) {
            response.add(mapToResponse(order));
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabOrderResponse> getOrdersByStatus(LabOrderStatus status) {

        List<LabOrder> orders =
                labOrderRepository.findByStatus(status);

        List<LabOrderResponse> response = new ArrayList<>();

        for (LabOrder order : orders) {
            response.add(mapToResponse(order));
        }

        return response;
    }

    @Override
    @Transactional
    public LabOrderResponse updateStatus(Long id,
                                         LabOrderStatus status) {

        LabOrder labOrder = labOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab Order not found with ID : " + id));

        labOrder.setStatus(status);

        LabOrder updated = labOrderRepository.save(labOrder);

        return mapToResponse(updated);
    }

    private LabOrderResponse mapToResponse(LabOrder labOrder) {

        Patient patient = labOrder.getAppointment().getPatient();

        return LabOrderResponse.builder()
                .id(labOrder.getId())
                .appointmentId(labOrder.getAppointment().getId())

                .patientName(
                        patient.getFirstName() + " " + patient.getLastName()
                )

                .labTestId(labOrder.getLabTest().getId())
                .labTestName(labOrder.getLabTest().getTestName())
                .clinicalNotes(labOrder.getClinicalNotes())
                .priority(labOrder.getPriority())
                .instructions(labOrder.getInstructions())
                .status(labOrder.getStatus())
                .createdAt(labOrder.getCreatedAt())
                .updatedAt(labOrder.getUpdatedAt())
                .build();
    }
}
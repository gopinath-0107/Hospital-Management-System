package com.hospital.serviceImpl;

import com.hospital.dto.PrescriptionMedicineResponse;
import com.hospital.dto.PrescriptionResponse;
import com.hospital.entity.*;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.DispenseStatus;
import com.hospital.enums.PrescriptionStatus;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.*;
import com.hospital.service.PharmacyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PharmacyServiceImpl implements PharmacyService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMedicineRepository prescriptionMedicineRepository;
    private final MedicineRepository medicineRepository;
    private final PharmacyDispenseRepository pharmacyDispenseRepository;
    private final DispensedMedicineRepository dispensedMedicineRepository;
    private final PharmacistRepository pharmacistRepository;
    private final ConsultationRepository consultationRepository;

    @Override
    public void dispenseMedicine(Long prescriptionId, Long pharmacistId) {

        // ==========================
        // 1. Find Prescription
        // ==========================
        Prescription prescription =
                prescriptionRepository.findById(prescriptionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prescription not found"
                                ));

        // ==========================
        // 2. Appointment Validation
        // ==========================
        Appointment appointment =
                prescription.getAppointment();

        if (appointment.getStatus() != AppointmentStatus.CONSULTATION_DONE) {

            throw new RuntimeException(
                    "Appointment is not CONSULTATION_DONE."
            );
        }

        // ==========================
        // 3. Consultation Validation
        // ==========================
        Consultation consultation =
                consultationRepository.findByAppointmentId(
                        appointment.getId()
                );

        if (consultation == null) {

            throw new RuntimeException(
                    "Consultation not completed."
            );
        }

        // ==========================
        // 4. Prescription Status
        // ==========================
        if (prescription.getStatus() != PrescriptionStatus.ISSUED) {

            throw new RuntimeException(
                    "Prescription is not eligible for dispensing."
            );
        }

        // ==========================
        // 5. Already Dispensed?
        // ==========================
        if (pharmacyDispenseRepository
                .findByPrescriptionId(prescriptionId)
                .isPresent()) {

            throw new RuntimeException(
                    "Prescription already dispensed."
            );
        }

        // ==========================
        // 6. Prescription Medicines
        // ==========================
        List<PrescriptionMedicine> prescriptionMedicines =
                prescriptionMedicineRepository
                        .findByPrescriptionId(prescriptionId);

        if (prescriptionMedicines.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No medicines found in prescription."
            );
        }

        // ==========================
        // 7. Find Pharmacist
        // ==========================
        Pharmacist pharmacist =
                pharmacistRepository.findById(pharmacistId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pharmacist not found."
                                ));

        // ==========================
        // 8. Create Dispense Record
        // ==========================
        PharmacyDispense dispense =
                PharmacyDispense.builder()
                        .prescription(prescription)
                        .pharmacist(pharmacist)
                        .dispenseDate(LocalDateTime.now())
                        .status(DispenseStatus.PENDING)
                        .build();

        pharmacyDispenseRepository.save(dispense);

        // ==========================
        // 9. Reduce Medicine Stock
        // ==========================
        for (PrescriptionMedicine pm : prescriptionMedicines) {

            Medicine medicine =
                    pm.getMedicine();

            if(medicine.getExpiryDate().isBefore(LocalDate.now())){

                throw new RuntimeException(
                        "Medicine expired"
                );
            }

            if (medicine.getStockQuantity()
                    < pm.getQuantity()) {

                throw new RuntimeException(
                        medicine.getMedicineName()
                                + " stock not available."
                );
            }

            medicine.setStockQuantity(
                    medicine.getStockQuantity()
                            - pm.getQuantity()
            );

            medicineRepository.save(medicine);

            DispensedMedicine dispensedMedicine =
                    DispensedMedicine.builder()
                            .pharmacyDispense(dispense)
                            .medicine(medicine)
                            .quantity(pm.getQuantity())
                            .build();

            dispensedMedicineRepository.save(
                    dispensedMedicine
            );
        }

        // ==========================
        // 10. Update Status
        // ==========================
        dispense.setStatus(
                DispenseStatus.COMPLETED
        );

        pharmacyDispenseRepository.save(
                dispense
        );

        prescription.setStatus(
                PrescriptionStatus.DISPENSED
        );

        prescriptionRepository.save(
                prescription
        );
    }

    @Override
    public List<PrescriptionResponse> getPendingPrescriptions() {

        List<Prescription> prescriptions =
                prescriptionRepository.findByStatus(
                        PrescriptionStatus.ISSUED
                );

        return prescriptions.stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Convert Prescription Entity to Response DTO
     */
    private PrescriptionResponse mapToResponse(
            Prescription prescription) {

        PrescriptionResponse response =
                new PrescriptionResponse();

        // =============================
        // Basic Details
        // =============================
        response.setPrescriptionId(
                prescription.getId()
        );

        response.setAppointmentId(
                prescription.getAppointment().getId()
        );

        response.setDiagnosis(
                prescription.getDiagnosis()
        );

        response.setInstructions(
                prescription.getInstructions()
        );

        response.setStatus(
                prescription.getStatus().name()
        );

        response.setCreatedAt(
                prescription.getCreatedAt()
        );

        // =============================
        // Patient Details
        // =============================
        Patient patient =
                prescription.getAppointment()
                        .getPatient();

        response.setPatientName(
                patient.getFirstName()
                        + " "
                        + patient.getLastName()
        );

        // =============================
        // Doctor Details
        // =============================
        Doctor doctor =
                prescription.getAppointment()
                        .getDoctor();

        response.setDoctorName(
                "Dr. "
                        + doctor.getFirstName()
                        + " "
                        + doctor.getLastName()
        );

        // =============================
        // Medicines
        // =============================
        List<PrescriptionMedicineResponse> medicines =
                prescription.getMedicines()
                        .stream()
                        .map(this::mapMedicine)
                        .toList();

        response.setMedicines(
                medicines
        );

        return response;
    }


    private PrescriptionMedicineResponse mapMedicine(
            PrescriptionMedicine medicine) {

        PrescriptionMedicineResponse response =
                new PrescriptionMedicineResponse();

        response.setMedicineId(
                medicine.getMedicine().getId()
        );

        response.setMedicineName(
                medicine.getMedicine()
                        .getMedicineName()
        );

        response.setDosage(
                medicine.getDosage()
        );

        response.setFrequency(
                medicine.getFrequency()
        );

        response.setDuration(
                medicine.getDuration() + " days"
        );

        response.setQuantity(
                medicine.getQuantity()
        );

        return response;
    }

}
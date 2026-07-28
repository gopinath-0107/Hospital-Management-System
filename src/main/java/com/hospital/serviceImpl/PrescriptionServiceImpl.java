package com.hospital.serviceImpl;

import com.hospital.dto.*;
import com.hospital.dto.PrescriptionMedicineResponse;
import com.hospital.entity.*;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.NotificationType;
import com.hospital.enums.PrescriptionStatus;
import com.hospital.enums.Role;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.repo.*;
import com.hospital.service.HospitalNotificationService;
import com.hospital.service.PrescriptionService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {


    private final PrescriptionRepository prescriptionRepository;

    private final PrescriptionMedicineRepository prescriptionMedicineRepository;

    private final AppointmentRepository appointmentRepository;

    private final MedicineRepository medicineRepository;

    private final ConsultationRepository consultationRepository;

    private final HospitalNotificationService hospitalNotificationService;

    @Override
    @Transactional
    public PrescriptionResponse createPrescription(PrescriptionRequest request) {

        Appointment appointment =
                appointmentRepository.findById(request.getAppointmentId())
                        .orElseThrow(() ->
                                new DuplicateResourceException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.CONSULTATION_DONE) {

            throw new RuntimeException(
                    "Consultation not completed"
            );
        }

        Consultation consultation =
                consultationRepository.findByAppointmentId(
                        request.getAppointmentId()
                );

        if (consultation == null) {

            throw new IllegalStateException(
                    "Please complete consultation before creating prescription."
            );
        }

        boolean exists =
                prescriptionRepository
                        .findByAppointmentId(request.getAppointmentId())
                        .isPresent();

        if (exists) {

            throw new DuplicateResourceException(
                    "Prescription already exists for this appointment"
            );
        }

        Prescription prescription =
                Prescription.builder()
                        .appointment(appointment)
                        .diagnosis(consultation.getDiagnosis())
                        .instructions(request.getInstructions())
                        .status(PrescriptionStatus.ISSUED)
                        .build();

        List<PrescriptionMedicine> medicines =
                new ArrayList<>();

        for (PrescriptionMedicineRequest medicineRequest :
                request.getMedicines()) {

            Medicine medicine =
                    medicineRepository
                            .findById(medicineRequest.getMedicineId())
                            .orElseThrow(() ->
                                    new DuplicateResourceException(
                                            "Medicine not found"
                                    ));

            PrescriptionMedicine prescriptionMedicine =
                    PrescriptionMedicine.builder()
                            .prescription(prescription)
                            .medicine(medicine)
                            .dosage(medicineRequest.getDosage())
                            .quantity(medicineRequest.getQuantity())
                            .frequency(medicineRequest.getFrequency())
                            .duration(medicineRequest.getDuration())
                            .build();

            medicines.add(prescriptionMedicine);
        }

        prescription.setMedicines(medicines);

        Prescription savedPrescription =
                prescriptionRepository.save(prescription);

        // =====================================
        // Notification to Patient
        // =====================================

        hospitalNotificationService.createNotification(
                appointment.getPatient().getId(),
                Role.PATIENT,
                NotificationType.PRESCRIPTION,
                "Prescription Ready",
                "Your prescription has been prepared by Dr. "
                        + appointment.getDoctor().getFirstName()
                        + " "
                        + appointment.getDoctor().getLastName()
                        + ". Please collect your medicines from the pharmacy."
        );

        return mapToResponse(savedPrescription);
    }
    @Override
    public PrescriptionResponse getPrescriptionById(Long id) {


        Prescription prescription =
                prescriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new DuplicateResourceException(
                                        "Prescription not found"
                                )
                        );


        return mapToResponse(prescription);

    }



    @Override
    public PrescriptionResponse getPrescriptionByAppointment(Long appointmentId) {


        Prescription prescription =
                prescriptionRepository
                        .findByAppointmentId(appointmentId)
                        .orElseThrow(() ->
                                new DuplicateResourceException(
                                        "Prescription not found"
                                )
                        );


        return mapToResponse(prescription);

    }




    @Override
    public List<PrescriptionResponse> getAllPrescriptions() {


        List<Prescription> prescriptions =
                prescriptionRepository.findAll();


        List<PrescriptionResponse> response =
                new ArrayList<>();


        for(Prescription prescription : prescriptions){

            response.add(
                    mapToResponse(prescription)
            );
        }


        return response;

    }





    private PrescriptionResponse mapToResponse(
            Prescription prescription) {


        List<PrescriptionMedicineResponse> medicineResponses =
                new ArrayList<>();


        for(PrescriptionMedicine medicine :
                prescription.getMedicines()) {


            PrescriptionMedicineResponse medicineResponse =
                    new PrescriptionMedicineResponse();


            medicineResponse.setMedicineId(
                    medicine.getMedicine().getId()
            );


            medicineResponse.setMedicineName(
                    medicine.getMedicine().getMedicineName()
            );


            medicineResponse.setDosage(
                    medicine.getDosage()
            );


            medicineResponse.setFrequency(
                    medicine.getFrequency()
            );


            medicineResponse.setDuration(
                    medicine.getDuration() + " days"
            );


            medicineResponse.setQuantity(
                    medicine.getQuantity()
            );


            medicineResponses.add(medicineResponse);

        }




        PrescriptionResponse response =
                new PrescriptionResponse();


        response.setPrescriptionId(
                prescription.getId()
        );


        response.setAppointmentId(
                prescription.getAppointment().getId()
        );


        response.setPatientName(
                prescription.getAppointment()
                        .getPatient()
                        .getFirstName()
                        + " " +
                        prescription.getAppointment()
                                .getPatient()
                                .getLastName()
        );

        response.setDoctorName(
                "Dr. " +
                        prescription.getAppointment()
                                .getDoctor()
                                .getFirstName()
                        + " " +
                        prescription.getAppointment()
                                .getDoctor()
                                .getLastName()
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


        response.setMedicines(
                medicineResponses
        );


        return response;
    }
    @Override
    public void updatePrescriptionStatus(
            Long prescriptionId,
            PrescriptionStatus status) {


        Prescription prescription =
                prescriptionRepository.findById(prescriptionId)
                        .orElseThrow(() ->
                                new DuplicateResourceException(
                                        "Prescription not found")
                        );


        if (prescription.getStatus() == PrescriptionStatus.DISPENSED) {

            throw new IllegalStateException(
                    "Prescription already dispensed."
            );

        }

        prescription.setStatus(
                PrescriptionStatus.DISPENSED
        );

        prescriptionRepository.save(prescription);

    }

}
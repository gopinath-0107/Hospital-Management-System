package com.hospital.serviceImpl;

import com.hospital.dto.*;
import com.hospital.entity.Payment;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.LabOrderStatus;
import com.hospital.enums.LabReportStatus;
import com.hospital.enums.PaymentStatus;
import com.hospital.repo.*;
import com.hospital.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final NurseRepository nurseRepository;

    private final ReceptionistRepository receptionistRepository;

    private final LabTechnicianRepository labTechnicianRepository;

    private final PharmacistRepository pharmacistRepository;

    private final AppointmentRepository appointmentRepository;

    private final ConsultationRepository consultationRepository;

    private final PrescriptionRepository prescriptionRepository;

    private final MedicineRepository medicineRepository;

    private final PharmacyDispenseRepository pharmacyDispenseRepository;

    private final LabOrderRepository labOrderRepository;

    private final LabReportRepository labReportRepository;

    private final PaymentRepository paymentRepository;

    // ==========================
    // ADMIN DASHBOARD
    // ==========================

    @Override
    public AdminDashboardResponse getAdminDashboard() {

        BigDecimal totalRevenue = BigDecimal.ZERO;

        List<Payment> payments = paymentRepository.findAll();

        for (Payment payment : payments) {

            if (payment.getPaymentStatus() == PaymentStatus.PAID
                    && payment.getAmount() != null) {

                totalRevenue = totalRevenue.add(payment.getAmount());
            }
        }

        return AdminDashboardResponse.builder()

                .totalPatients(
                        patientRepository.count()
                )

                .totalDoctors(
                        doctorRepository.count()
                )

                .totalNurses(
                        nurseRepository.count()
                )

                .totalReceptionists(
                        receptionistRepository.count()
                )

                .totalLabTechnicians(
                        labTechnicianRepository.count()
                )

                .totalPharmacists(
                        pharmacistRepository.count()
                )

                .totalAppointments(
                        appointmentRepository.count()
                )

                .totalMedicines(
                        medicineRepository.count()
                )

                .totalRevenue(
                        totalRevenue
                )

                .build();
    }

    // ==========================
    // DOCTOR DASHBOARD
    // ==========================

    @Override
    public DoctorDashboardResponse getDoctorDashboard(Long doctorId) {

        long totalAppointments =
                appointmentRepository.countByDoctorId(doctorId);

        long totalConsultations =
                consultationRepository.countByDoctorId(doctorId);

        long totalPrescriptions =
                prescriptionRepository
                        .countByAppointmentDoctorId(doctorId);

        long totalPatients = totalAppointments;

        return DoctorDashboardResponse.builder()

                .totalAppointments(
                        totalAppointments
                )

                .totalPatients(
                        totalPatients
                )

                .totalConsultations(
                        totalConsultations
                )

                .totalPrescriptions(
                        totalPrescriptions
                )

                .build();
    }
    // ==========================
    // PHARMACY DASHBOARD
    // ==========================

    @Override
    public PharmacyDashboardResponse getPharmacyDashboard() {

        Long totalMedicineStock =
                medicineRepository.getTotalMedicineStock();

        long lowStockMedicines =
                medicineRepository.countByStockQuantityLessThan(10);

        long totalDispensed =
                pharmacyDispenseRepository.count();

        return PharmacyDashboardResponse.builder()

                .totalMedicines(
                        totalMedicineStock
                )

                .lowStockMedicines(
                        lowStockMedicines
                )

                .totalDispensed(
                        totalDispensed
                )

                .build();
    }

    // ==========================
    // LAB DASHBOARD
    // ==========================

    @Override
    public LabDashboardResponse getLabDashboard() {

        long totalLabOrders =
                labOrderRepository.count();

        long pendingTests =
                labOrderRepository.countByStatus(
                        LabOrderStatus.PENDING
                );

        long completedReports =
                labReportRepository.countByStatus(
                        LabReportStatus.UPLOADED
                );

        long totalReports =
                labReportRepository.count();

        return LabDashboardResponse.builder()

                .totalLabOrders(
                        totalLabOrders
                )

                .pendingTests(
                        pendingTests
                )

                .completedReports(
                        completedReports
                )

                .totalReports(
                        totalReports
                )

                .build();
    }


    @Override
    public PatientDashboardResponse getPatientDashboard(Long patientId) {


        long totalAppointments =
                appointmentRepository.countByPatientId(patientId);


        long upcomingAppointments =
                appointmentRepository.countByPatientIdAndStatus(
                        patientId,
                        AppointmentStatus.APPROVED
                );


        long completedAppointments =
                appointmentRepository.countByPatientIdAndStatus(
                        patientId,
                        AppointmentStatus.COMPLETED
                );


        long cancelledAppointments =
                appointmentRepository.countByPatientIdAndStatus(
                        patientId,
                        AppointmentStatus.REJECTED
                );


        long totalPrescriptions =
                prescriptionRepository
                        .countByAppointmentPatientId(patientId);


        long totalLabTests =
                labOrderRepository
                        .countByAppointmentPatientId(patientId);


        long totalLabReports =
                labReportRepository
                        .countByLabOrderAppointmentPatientId(patientId);


        long totalBills =
                paymentRepository
                        .countByAppointmentPatientId(patientId);


        long paidBills =
                paymentRepository
                        .countByAppointmentPatientIdAndPaymentStatus(
                                patientId,
                                PaymentStatus.PAID
                        );


        return PatientDashboardResponse.builder()

                .totalAppointments(totalAppointments)

                .upcomingAppointments(upcomingAppointments)

                .completedAppointments(completedAppointments)

                .cancelledAppointments(cancelledAppointments)

                .totalPrescriptions(totalPrescriptions)

                .totalLabTests(totalLabTests)

                .totalLabReports(totalLabReports)

                .totalBills(totalBills)

                .paidBills(paidBills)

                .build();
    }


    @Override
    public ReceptionistDashboardResponse getReceptionistDashboard(){


        return ReceptionistDashboardResponse.builder()


                .totalPatients(
                        patientRepository.count()
                )


                .totalAppointments(
                        appointmentRepository.count()
                )


                .pendingAppointments(
                        appointmentRepository.countByStatus(
                                AppointmentStatus.PENDING
                        )
                )


                .approvedAppointments(
                        appointmentRepository.countByStatus(
                                AppointmentStatus.APPROVED
                        )
                )


                .completedAppointments(
                        appointmentRepository.countByStatus(
                                AppointmentStatus.COMPLETED
                        )
                )


                .cancelledAppointments(
                        appointmentRepository.countByStatus(
                                AppointmentStatus.REJECTED
                        )
                )


                .build();
    }

}
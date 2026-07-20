package com.hospital.serviceImpl;


import com.hospital.dto.AdminDashboardResponse;
import com.hospital.dto.DoctorDashboardResponse;
import com.hospital.dto.LabDashboardResponse;
import com.hospital.dto.PharmacyDashboardResponse;
import com.hospital.entity.Billing;
import com.hospital.enums.BillingStatus;
import com.hospital.enums.LabOrderStatus;
import com.hospital.enums.LabReportStatus;
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


    private final LabOrderRepository labOrderRepository;

    private final LabReportRepository labReportRepository;


    private final AppointmentRepository appointmentRepository;

    private final ConsultationRepository consultationRepository;

    private final PrescriptionRepository prescriptionRepository;


    private final MedicineRepository medicineRepository;

    private final PharmacyDispenseRepository pharmacyDispenseRepository;


    private final BillingRepository billingRepository;



    // ================= ADMIN DASHBOARD =================

    @Override
    public AdminDashboardResponse getAdminDashboard() {


        BigDecimal totalRevenue = BigDecimal.ZERO;


        List<Billing> paidBills =
                billingRepository.findByBillingStatus(BillingStatus.PAID);



        for (Billing billing : paidBills) {


            if (billing.getTotalAmount() != null) {

                totalRevenue =
                        totalRevenue.add(
                                billing.getTotalAmount()
                        );
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





    // ================= DOCTOR DASHBOARD =================


    @Override
    public DoctorDashboardResponse getDoctorDashboard(Long doctorId) {


        long totalAppointments =
                appointmentRepository
                        .countByDoctorId(doctorId);



        long totalConsultations =
                consultationRepository
                        .countByDoctorId(doctorId);



        long totalPrescriptions =
                prescriptionRepository
                        .countByAppointmentDoctorId(doctorId);



        long totalPatients =
                totalAppointments;



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






    // ================= PHARMACY DASHBOARD =================


    @Override
    public PharmacyDashboardResponse getPharmacyDashboard() {


        Long totalMedicineStock =
                medicineRepository.getTotalMedicineStock();


        long lowStockMedicines =
                medicineRepository.countByStockQuantityLessThan(10);


        long totalDispensed =
                pharmacyDispenseRepository.count();




        return PharmacyDashboardResponse.builder()

                .totalMedicines(totalMedicineStock)

                .lowStockMedicines(
                        lowStockMedicines
                )

                .totalDispensed(
                        totalDispensed
                )

                .build();

    }







    // ================= LAB DASHBOARD =================


    @Override
    public LabDashboardResponse getLabDashboard() {



        long totalLabOrders =
                labOrderRepository.count();



        long pendingTests =
                labOrderRepository
                        .countByStatus(
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


}
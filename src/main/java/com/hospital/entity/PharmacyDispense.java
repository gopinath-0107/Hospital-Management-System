package com.hospital.entity;

import com.hospital.enums.DispenseStatus;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name="pharmacy_dispense")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyDispense {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(
            name = "prescription_id",
            nullable = false,
            unique = true
    )
    private Prescription prescription;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacist_id", nullable = false)
    private Pharmacist pharmacist;


    @Column(nullable = false)
    private LocalDateTime dispenseDate;


    @Enumerated(EnumType.STRING)
    private DispenseStatus status;

}
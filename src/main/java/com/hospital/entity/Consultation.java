package com.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@Table(name = "consultations")
public class Consultation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;


    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;


    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;


    @ManyToMany
    @JoinTable(
            name = "consultation_symptoms",
            joinColumns = @JoinColumn(name = "consultation_id"),
            inverseJoinColumns = @JoinColumn(name = "symptom_id")
    )
    private List<Symptom> symptoms;


    private String bloodPressure;

    private Double temperature;

    private Integer pulseRate;


    @Column(columnDefinition = "TEXT")
    private String diagnosis;


    @Column(columnDefinition = "TEXT")
    private String notes;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    @PrePersist
    public void onCreate(){

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

    }


    @PreUpdate
    public void onUpdate(){

        updatedAt = LocalDateTime.now();

    }

}
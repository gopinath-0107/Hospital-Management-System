package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voice_consultations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceConsultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "appointment_id", unique = true)
    private Appointment appointment;

    @Column(columnDefinition = "TEXT")
    private String transcription;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "blood_pressure")
    private String bloodPressure;

    private Double temperature;

    private Integer pulse;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

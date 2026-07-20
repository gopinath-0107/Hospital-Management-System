package com.hospital.entity;

import com.hospital.enums.Gender;
import com.hospital.enums.Role;
import com.hospital.enums.Shift;
import com.hospital.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "nurses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nurse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String firstName;


    @Column(nullable = false)
    private String lastName;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false, unique = true, length = 15)
    private String mobile;


    @Column(nullable = false, length = 255)
    private String password;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;


    @Column(nullable = false)
    private String qualification;


    @Column(nullable = false)
    private Integer experience;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Shift shift;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
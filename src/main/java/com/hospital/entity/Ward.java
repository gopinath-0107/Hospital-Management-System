package com.hospital.entity;

import com.hospital.enums.WardStatus;
import com.hospital.enums.WardType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "wards",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "ward_name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ward_name", nullable = false, unique = true, length = 100)
    private String wardName;

    @Enumerated(EnumType.STRING)
    @Column(name = "ward_type", nullable = false)
    private WardType wardType;

    @Column(nullable = false)
    @Min(0)
    private Integer floor;

    @Column(name = "total_rooms", nullable = false)
    @Min(1)
    private Integer totalRooms;

    @Column(length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WardStatus status;

    @OneToMany(mappedBy = "ward",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
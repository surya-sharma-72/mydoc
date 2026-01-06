package com.app.mydoc.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String patientName;
    private String phoneNumber;
    private String issue;

    private LocalDate appointmentDate;

    @Enumerated(EnumType.STRING)
    private TimeSlot timeSlot;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    // 🔗 Relations via IDs (not JPA relations)
    private UUID doctorId;
    private UUID departmentId;
    private UUID hospitalId;

    // Who booked it
    private UUID createdByUserId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.app.mydoc.dto;

import com.app.mydoc.entity.AppointmentStatus;
import com.app.mydoc.entity.TimeSlot;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AppointmentResponse {

    private UUID id;
    private String patientName;
    private String phoneNumber;
    private String issue;

    private LocalDate appointmentDate;
    private TimeSlot timeSlot;
    private AppointmentStatus status;

    private UUID doctorId;
    private UUID departmentId;
    private UUID hospitalId;
}

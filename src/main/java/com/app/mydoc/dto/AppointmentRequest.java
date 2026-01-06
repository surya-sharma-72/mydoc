package com.app.mydoc.dto;

import com.app.mydoc.entity.TimeSlot;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AppointmentRequest {

    private String patientName;
    private String phoneNumber;
    private String issue;

    private LocalDate appointmentDate;
    private TimeSlot timeSlot;

    private UUID doctorId;
    private UUID departmentId;
    private UUID hospitalId;
}

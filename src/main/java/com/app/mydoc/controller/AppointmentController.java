package com.app.mydoc.controller;

import com.app.mydoc.dto.*;
import com.app.mydoc.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // USER → BOOK
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/book")
    public AppointmentResponse book(@RequestBody AppointmentRequest request) {
        return appointmentService.bookAppointment(request);
    }

    // USER → MY APPOINTMENTS
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public List<AppointmentResponse> myAppointments() {
        return appointmentService.getMyAppointments();
    }

    // ADMIN → HOSPITAL APPOINTMENTS
    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN')")
    @GetMapping("/hospital")
    public List<AppointmentResponse> hospitalAppointments() {
        return appointmentService.getHospitalAppointments();
    }

    // ADMIN → APPROVE / REJECT
    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN')")
    @PutMapping("/{id}/status")
    public AppointmentResponse updateStatus(
            @PathVariable UUID id,
            @RequestBody AppointmentStatusUpdateRequest request
    ) {
        return appointmentService.updateStatus(id, request.getStatus());
    }

    // USER → CANCEL
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/cancel")
    public String cancel(@PathVariable UUID id) {
        appointmentService.cancelAppointment(id);
        return "Appointment cancelled";
    }
}

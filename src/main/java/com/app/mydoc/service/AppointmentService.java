package com.app.mydoc.service;

import com.app.mydoc.dto.*;
import com.app.mydoc.entity.Appointment;
import com.app.mydoc.entity.AppointmentStatus;
import com.app.mydoc.repository.AppointmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final HttpServletRequest request;

    // -------------------------
    // USER → BOOK
    // -------------------------
    public AppointmentResponse bookAppointment(AppointmentRequest req) {

        UUID userId = getCurrentUserId();

        Appointment appointment = Appointment.builder()
                .patientName(req.getPatientName())
                .phoneNumber(req.getPhoneNumber())
                .issue(req.getIssue())
                .appointmentDate(req.getAppointmentDate())
                .timeSlot(req.getTimeSlot())
                .doctorId(req.getDoctorId())
                .departmentId(req.getDepartmentId())
                .hospitalId(req.getHospitalId())
                .createdByUserId(userId)
                .status(AppointmentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return map(appointmentRepository.save(appointment));
    }

    // -------------------------
    // USER → MY APPOINTMENTS
    // -------------------------
    public List<AppointmentResponse> getMyAppointments() {
        return appointmentRepository
                .findByCreatedByUserId(getCurrentUserId())
                .stream()
                .map(this::map)
                .toList();
    }

    // -------------------------
    // ADMIN → HOSPITAL APPOINTMENTS
    // -------------------------
    public List<AppointmentResponse> getHospitalAppointments() {

        UUID hospitalId = getHospitalIdFromJwt();

        return appointmentRepository
                .findByHospitalId(hospitalId)
                .stream()
                .map(this::map)
                .toList();
    }

    // -------------------------
// ADMIN → APPROVE / REJECT (PUT, NULL-SAFE)
// -------------------------
    public AppointmentResponse updateStatus(
            UUID appointmentId,
            AppointmentStatus status
    ) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        validateHospitalAccess(appointment.getHospitalId());

        // ✅ Only update if frontend actually sent a value
        if (status != null && status != appointment.getStatus()) {
            appointment.setStatus(status);
            appointment.setUpdatedAt(LocalDateTime.now());
        }

        // ❌ Nothing else is touched → no null overwrites
        return map(appointmentRepository.save(appointment));
    }

    // -------------------------
// USER → CANCEL (PUT, SAFE)
// -------------------------
    public AppointmentResponse cancelAppointment(UUID appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getCreatedByUserId().equals(getCurrentUserId())) {
            throw new RuntimeException("Unauthorized cancellation");
        }

        // ✅ Update only if not already cancelled
        if (appointment.getStatus() != AppointmentStatus.CANCELLED) {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointment.setUpdatedAt(LocalDateTime.now());
        }

        // ❌ No other fields touched
        return map(appointmentRepository.save(appointment));
    }


    // -------------------------
    // HELPERS
    // -------------------------
    private UUID getCurrentUserId() {
        return (UUID) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private UUID getHospitalIdFromJwt() {
        return UUID.fromString(
                request.getAttribute("hospitalId").toString()
        );
    }

    private void validateHospitalAccess(UUID targetHospitalId) {

        Object hospitalIdAttr = request.getAttribute("hospitalId");

        if (hospitalIdAttr == null) return; // SUPERADMIN

        UUID loggedHospitalId = UUID.fromString(hospitalIdAttr.toString());

        if (!loggedHospitalId.equals(targetHospitalId)) {
            throw new RuntimeException("Unauthorized hospital access");
        }
    }

    private AppointmentResponse map(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .patientName(a.getPatientName())
                .phoneNumber(a.getPhoneNumber())
                .issue(a.getIssue())
                .appointmentDate(a.getAppointmentDate())
                .timeSlot(a.getTimeSlot())
                .status(a.getStatus())
                .doctorId(a.getDoctorId())
                .departmentId(a.getDepartmentId())
                .hospitalId(a.getHospitalId())
                .build();
    }
}

package com.app.mydoc.repository;

import com.app.mydoc.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByCreatedByUserId(UUID userId);

    List<Appointment> findByHospitalId(UUID hospitalId);

    List<Appointment> findByDoctorId(UUID doctorId);
}

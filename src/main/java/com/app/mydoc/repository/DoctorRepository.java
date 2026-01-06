package com.app.mydoc.repository;

import com.app.mydoc.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    List<Doctor> findByDepartmentId(UUID departmentId);

    List<Doctor> findByDepartment_Hospital_Id(UUID hospitalId);
}

package com.app.mydoc.service;

import com.app.mydoc.dto.DepartmentRequest;
import com.app.mydoc.dto.DepartmentResponse;
import com.app.mydoc.entity.Department;
import com.app.mydoc.entity.Hospital;
import com.app.mydoc.repository.DepartmentRepository;
import com.app.mydoc.repository.HospitalRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final HospitalRepository hospitalRepository;
    private final HttpServletRequest request;

    // -------------------------
    // CREATE
    // -------------------------
    public String addDepartment(DepartmentRequest requestDto) {

        Hospital hospital = hospitalRepository.findById(requestDto.getHospitalId())
                .orElseThrow(() -> new RuntimeException("Hospital not found"));

        validateHospitalAccess(hospital.getId());

        Department department = Department.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .hospital(hospital)
                .build();

        departmentRepository.save(department);

        return "Department added successfully";
    }

    // -------------------------
    // GET by hospital
    // -------------------------
    public List<DepartmentResponse> getDepartmentsByHospital(UUID hospitalId) {

        return departmentRepository.findByHospitalId(hospitalId)
                .stream()
                .map(dep -> DepartmentResponse.builder()
                        .id(dep.getId())
                        .name(dep.getName())
                        .description(dep.getDescription())
                        .hospitalId(dep.getHospital().getId())
                        .build())
                .toList();
    }

    // -------------------------
    // GET by id
    // -------------------------
    public DepartmentResponse getDepartmentById(UUID id) {

        Department dep = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        return DepartmentResponse.builder()
                .id(dep.getId())
                .name(dep.getName())
                .description(dep.getDescription())
                .hospitalId(dep.getHospital().getId())
                .build();
    }

    // -------------------------
    // DELETE
    // -------------------------
    public String deleteDepartment(UUID departmentId) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        validateHospitalAccess(department.getHospital().getId());

        departmentRepository.delete(department);
        return "Department deleted successfully";
    }

    // -------------------------
    // UPDATE
    // -------------------------
    public DepartmentResponse updateDepartment(
            UUID departmentId,
            DepartmentRequest requestDto) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        validateHospitalAccess(department.getHospital().getId());

        if (requestDto.getName() != null) {
            department.setName(requestDto.getName());
        }

        if (requestDto.getDescription() != null) {
            department.setDescription(requestDto.getDescription());
        }

        Department updated = departmentRepository.save(department);

        return DepartmentResponse.builder()
                .id(updated.getId())
                .name(updated.getName())
                .description(updated.getDescription())
                .hospitalId(updated.getHospital().getId())
                .build();
    }

    // -------------------------
    // HOSPITAL ACCESS VALIDATION
    // -------------------------
    private void validateHospitalAccess(UUID targetHospitalId) {

        Object hospitalIdAttr = request.getAttribute("hospitalId");

        // null → SUPERADMIN
        if (hospitalIdAttr == null) {
            return;
        }

        UUID loggedInHospitalId = UUID.fromString(hospitalIdAttr.toString());

        if (!loggedInHospitalId.equals(targetHospitalId)) {
            throw new RuntimeException("Unauthorized: Access denied for this hospital");
        }
    }
}

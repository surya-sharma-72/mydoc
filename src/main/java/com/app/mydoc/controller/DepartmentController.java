package com.app.mydoc.controller;

import com.app.mydoc.dto.DepartmentRequest;
import com.app.mydoc.dto.DepartmentResponse;
import com.app.mydoc.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    // -------------------------
    // ADD department
    // ADMIN / SUPERADMIN
    // -------------------------
    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN')")
    @PostMapping("/add")
    public String addDepartment(@RequestBody DepartmentRequest request) {
        return departmentService.addDepartment(request);
    }

    // -------------------------
    // GET departments by hospital
    // USER / ADMIN / SUPERADMIN
    // -------------------------
    @PreAuthorize("hasAnyRole('USER','RECEPTIONIST','ADMIN')")
    @GetMapping("/hospital/{hospitalId}")
    public List<DepartmentResponse> getDepartments(
            @PathVariable UUID hospitalId) {
        return departmentService.getDepartmentsByHospital(hospitalId);
    }

    // -------------------------
    // GET by id
    // USER / ADMIN / SUPERADMIN
    // -------------------------
    @PreAuthorize("hasAnyRole('USER','RECEPTIONIST','ADMIN')")
    @GetMapping("/{id}")
    public DepartmentResponse getDepartmentById(@PathVariable UUID id) {
        return departmentService.getDepartmentById(id);
    }

    // -------------------------
    // DELETE department
    // ADMIN / SUPERADMIN
    // -------------------------
    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable UUID id) {
        return departmentService.deleteDepartment(id);
    }

    // -------------------------
    // UPDATE department
    // ADMIN / SUPERADMIN
    // -------------------------
    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN')")
    @PutMapping("/update/{id}")
    public DepartmentResponse updateDepartment(
            @PathVariable UUID id,
            @RequestBody DepartmentRequest request) {

        return departmentService.updateDepartment(id, request);
    }
}

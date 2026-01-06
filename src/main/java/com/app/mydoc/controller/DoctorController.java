package com.app.mydoc.controller;

import com.app.mydoc.dto.DoctorRequest;
import com.app.mydoc.dto.DoctorResponse;
import com.app.mydoc.service.DoctorService;
import com.app.mydoc.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final JwtService jwtService;

    // -------------------------
    // CREATE doctor
    // ADMIN / SUPERADMIN
    // -------------------------
    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN')")
    @PostMapping(
            value = "/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public DoctorResponse addDoctor(
            @RequestPart("doctor") DoctorRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return doctorService.createDoctor(request, image);
    }

    // -------------------------
    // UPDATE doctor
    // ADMIN / SUPERADMIN
    // -------------------------
    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN')")
    @PutMapping(
            value = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public DoctorResponse updateDoctor(
            @PathVariable UUID id,
            @RequestPart("doctor") DoctorRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return doctorService.updateDoctor(id, request, image);
    }

    // -------------------------
    // GET by id
    // USER / ADMIN / SUPERADMIN
    // -------------------------
    @PreAuthorize("hasAnyRole('USER','RECEPTIONIST','ADMIN')")
    @GetMapping("/{id}")
    public DoctorResponse getDoctorById(@PathVariable UUID id) {
        return doctorService.getDoctorById(id);
    }

    // -------------------------
    // GET by department
    // USER / ADMIN / SUPERADMIN
    // -------------------------
    @PreAuthorize("hasAnyRole('USER','RECEPTIONIST','ADMIN')")
    @GetMapping("/department/{departmentId}")
    public List<DoctorResponse> getDoctorsByDepartment(
            @PathVariable UUID departmentId
    ) {
        return doctorService.getDoctorsByDepartment(departmentId);
    }

//    // -------------------------
//    // GET by hospital
//    // USER / ADMIN / SUPERADMIN
//    // -------------------------
//    @PreAuthorize("hasAnyRole('USER','RECEPTIONIST','ADMIN')")
//    @GetMapping("/hospital/{hospitalId}")
//    public List<DoctorResponse> getDoctorsByHospital(
//            @PathVariable UUID hospitalId
//    ) {
//        return doctorService.getDoctorsByHospital(hospitalId);
//    }



    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN')")
    @GetMapping("/hospital")
    public List<DoctorResponse> getDoctorsByHospital(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7); // remove "Bearer "
        UUID hospitalId = jwtService.extractHospitalId(token);

        if (hospitalId == null) {
            throw new RuntimeException("Hospital ID not found in token");
        }

        return doctorService.getDoctorsByHospital(hospitalId);
    }



    // -------------------------
    // DELETE doctor
    // ADMIN / SUPERADMIN
    // -------------------------
    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);
        return "Doctor deleted successfully";
    }
}

package com.app.mydoc.controller;

import com.app.mydoc.dto.HospitalRequest;
import com.app.mydoc.dto.HospitalResponse;
import com.app.mydoc.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    // SUPERADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
            value = "/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public HospitalResponse createHospital(
            @RequestPart("hospital") HospitalRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return hospitalService.createHospital(request, image);
    }

    // USER, ADMIN, SUPERADMIN
    @PreAuthorize("hasAnyRole('USER','RECEPTIONIST','ADMIN')")
    @GetMapping("/all")
    public List<HospitalResponse> getAllHospitals() {
        return hospitalService.getAllHospitals();
    }

    // USER, ADMIN, SUPERADMIN
    @PreAuthorize("hasAnyRole('USER','RECEPTIONIST','ADMIN')")
    @GetMapping("/{id}")
    public HospitalResponse getHospitalById(@PathVariable UUID id) {
        return hospitalService.getHospitalById(id);
    }

    // ADMIN (own hospital) or SUPERADMIN
    @PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN')")
    @PutMapping(
            value = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public HospitalResponse updateHospital(
            @PathVariable UUID id,
            @RequestPart("hospital") HospitalRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return hospitalService.updateHospital(id, request, image);
    }

    // SUPERADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteHospital(@PathVariable UUID id) {
        hospitalService.deleteHospital(id);
        return "Hospital deleted successfully";
    }
}

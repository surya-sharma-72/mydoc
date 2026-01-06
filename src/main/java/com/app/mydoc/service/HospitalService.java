package com.app.mydoc.service;

import com.app.mydoc.dto.HospitalRequest;
import com.app.mydoc.dto.HospitalResponse;
import com.app.mydoc.entity.Hospital;
import com.app.mydoc.repository.HospitalRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final StorageService storageService;
    private final HttpServletRequest request;

    // ------------------------
    // CREATE (SUPERADMIN only)
    // ------------------------
    public HospitalResponse createHospital(
            HospitalRequest requestDto,
            MultipartFile image
    ) {

        Hospital hospital = Hospital.builder()
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .city(requestDto.getCity())
                .numberOfDoctors(requestDto.getNumberOfDoctors())
                .numberOfBeds(requestDto.getNumberOfBeds())
                .ageOfHospital(requestDto.getAgeOfHospital())
                .rating(requestDto.getRating())
                .about(requestDto.getAbout())
                .contactNumber(requestDto.getContactNumber())
                .build();

        if (image != null && !image.isEmpty()) {
            String imageUrl = storageService.upload(image, "hospitals");
            hospital.setPicture(imageUrl);
        }

        Hospital saved = hospitalRepository.save(hospital);
        return mapToResponse(saved);
    }

    // ------------------------
    // GET ALL (PUBLIC AUTH)
    // ------------------------
    public List<HospitalResponse> getAllHospitals() {
        return hospitalRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ------------------------
    // GET BY ID
    // ------------------------
    public HospitalResponse getHospitalById(UUID id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospital not found"));
        return mapToResponse(hospital);
    }

    // ------------------------
    // UPDATE (ADMIN own hospital / SUPERADMIN)
    // ------------------------
    public HospitalResponse updateHospital(
            UUID hospitalId,
            HospitalRequest requestDto,
            MultipartFile image
    ) {

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital not found"));

        UUID loggedInHospitalId = getLoggedInHospitalId();

        // ADMIN → can update ONLY own hospital
        if (loggedInHospitalId != null && !loggedInHospitalId.equals(hospital.getId())) {
            throw new RuntimeException("Unauthorized: Cannot update another hospital");
        }

        // update fields only if not null
        if (requestDto.getName() != null) hospital.setName(requestDto.getName());
        if (requestDto.getAddress() != null) hospital.setAddress(requestDto.getAddress());
        if (requestDto.getCity() != null) hospital.setCity(requestDto.getCity());
        if (requestDto.getNumberOfDoctors() != null) hospital.setNumberOfDoctors(requestDto.getNumberOfDoctors());
        if (requestDto.getNumberOfBeds() != null) hospital.setNumberOfBeds(requestDto.getNumberOfBeds());
        if (requestDto.getAgeOfHospital() != null) hospital.setAgeOfHospital(requestDto.getAgeOfHospital());
        if (requestDto.getRating() != null) hospital.setRating(requestDto.getRating());
        if (requestDto.getAbout() != null) hospital.setAbout(requestDto.getAbout());
        if (requestDto.getContactNumber() != null) hospital.setContactNumber(requestDto.getContactNumber());

        // replace image if new one is provided
        if (image != null && !image.isEmpty()) {

            if (hospital.getPicture() != null) {
                storageService.delete(hospital.getPicture());
            }

            String imageUrl = storageService.upload(image, "hospitals");
            hospital.setPicture(imageUrl);
        }

        Hospital updated = hospitalRepository.save(hospital);
        return mapToResponse(updated);
    }

    // ------------------------
    // DELETE (SUPERADMIN only)
    // ------------------------
    public void deleteHospital(UUID id) {

        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospital not found"));

        if (hospital.getPicture() != null) {
            storageService.delete(hospital.getPicture());
        }

        hospitalRepository.delete(hospital);
    }

    // ------------------------
    // GET LOGGED-IN HOSPITAL ID
    // ------------------------
    private UUID getLoggedInHospitalId() {

        Object hospitalIdAttr = request.getAttribute("hospitalId");

        if (hospitalIdAttr == null) {
            return null; // SUPERADMIN or USER
        }

        return UUID.fromString(hospitalIdAttr.toString());
    }

    // ------------------------
    // MAPPER
    // ------------------------
    private HospitalResponse mapToResponse(Hospital hospital) {
        return HospitalResponse.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .address(hospital.getAddress())
                .city(hospital.getCity())
                .numberOfDoctors(hospital.getNumberOfDoctors())
                .numberOfBeds(hospital.getNumberOfBeds())
                .ageOfHospital(hospital.getAgeOfHospital())
                .rating(hospital.getRating())
                .about(hospital.getAbout())
                .picture(hospital.getPicture())
                .contactNumber(hospital.getContactNumber())
                .build();
    }
}

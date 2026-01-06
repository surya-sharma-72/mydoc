package com.app.mydoc.service;

import com.app.mydoc.dto.DoctorRequest;
import com.app.mydoc.dto.DoctorResponse;
import com.app.mydoc.entity.Department;
import com.app.mydoc.entity.Doctor;
import com.app.mydoc.repository.DepartmentRepository;
import com.app.mydoc.repository.DoctorRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final StorageService storageService;
    private final HttpServletRequest request;

    // -------------------------
    // CREATE doctor
    // -------------------------
    public DoctorResponse createDoctor(
            DoctorRequest requestDto,
            MultipartFile image
    ) {

        Department department = departmentRepository.findById(requestDto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        validateHospitalAccess(department.getHospital().getId());

        Doctor doctor = Doctor.builder()
                .name(requestDto.getName())
                .phone(requestDto.getPhone())
                .cabinNumber(requestDto.getCabinNumber())
                .specialization(requestDto.getSpecialization())
                .experience(requestDto.getExperience())
                .fee(requestDto.getFee())
                .education(requestDto.getEducation())
                .department(department)
                .build();

        if (image != null && !image.isEmpty()) {
            String imageUrl = storageService.upload(image, "doctors");
            doctor.setPicture(imageUrl);
        }

        return mapToResponse(doctorRepository.save(doctor));
    }

    // -------------------------
    // GET by department
    // -------------------------
    public List<DoctorResponse> getDoctorsByDepartment(UUID departmentId) {
        return doctorRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // -------------------------
    // GET by id
    // -------------------------
    public DoctorResponse getDoctorById(UUID id) {
        Doctor d = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return mapToResponse(d);
    }

    // -------------------------
    // GET by hospital
    // -------------------------
    public List<DoctorResponse> getDoctorsByHospital(UUID hospitalId) {
        return doctorRepository.findByDepartment_Hospital_Id(hospitalId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // -------------------------
    // UPDATE doctor
    // -------------------------
    public DoctorResponse updateDoctor(
            UUID doctorId,
            DoctorRequest requestDto,
            MultipartFile image
    ) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        validateHospitalAccess(doctor.getDepartment().getHospital().getId());

        if (requestDto.getName() != null) doctor.setName(requestDto.getName());
        if (requestDto.getPhone() != null) doctor.setPhone(requestDto.getPhone());
        if (requestDto.getCabinNumber() != null) doctor.setCabinNumber(requestDto.getCabinNumber());
        if (requestDto.getSpecialization() != null) doctor.setSpecialization(requestDto.getSpecialization());
        if (requestDto.getExperience() != null) doctor.setExperience(requestDto.getExperience());
        if (requestDto.getFee() != null) doctor.setFee(requestDto.getFee());
        if (requestDto.getEducation() != null) doctor.setEducation(requestDto.getEducation());

        if (image != null && !image.isEmpty()) {
            if (doctor.getPicture() != null) {
                storageService.delete(doctor.getPicture());
            }
            String imageUrl = storageService.upload(image, "doctors");
            doctor.setPicture(imageUrl);
        }

        return mapToResponse(doctorRepository.save(doctor));
    }

    // -------------------------
    // DELETE doctor
    // -------------------------
    public void deleteDoctor(UUID doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        validateHospitalAccess(doctor.getDepartment().getHospital().getId());

        if (doctor.getPicture() != null) {
            storageService.delete(doctor.getPicture());
        }

        doctorRepository.delete(doctor);
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

    // -------------------------
    // MAPPER
    // -------------------------
    private DoctorResponse mapToResponse(Doctor d) {
        return DoctorResponse.builder()
                .id(d.getId())
                .name(d.getName())
                .phone(d.getPhone())
                .cabinNumber(d.getCabinNumber())
                .specialization(d.getSpecialization())
                .experience(d.getExperience())
                .fee(d.getFee())
                .education(d.getEducation())
                .picture(d.getPicture())
                .departmentId(d.getDepartment().getId())
                .departmentName(d.getDepartment().getName())
                .hospitalName(d.getDepartment().getHospital().getName())
                .build();
    }
}

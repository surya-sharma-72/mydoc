package com.app.mydoc.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequest {

    private String name;
    private String phone;
    private String cabinNumber;
    private String specialization;
    private Integer experience;
    private Double fee;
    private String education;
    private String picture;

    private UUID departmentId;
}

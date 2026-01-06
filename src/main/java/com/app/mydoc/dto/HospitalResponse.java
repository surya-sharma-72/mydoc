package com.app.mydoc.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalResponse {

    private UUID id;
    private String name;
    private String address;
    private String city;
    private Integer numberOfDoctors;
    private Integer numberOfBeds;
    private Integer ageOfHospital;
    private Double rating;
    private String about;
    private String picture;
    private String contactNumber;
}

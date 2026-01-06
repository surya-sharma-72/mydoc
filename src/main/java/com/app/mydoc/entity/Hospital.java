package com.app.mydoc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "hospitals",
        uniqueConstraints = {@UniqueConstraint(columnNames = "name")}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospital {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String city;

    private Integer numberOfDoctors;

    private Integer numberOfBeds;

    private Integer ageOfHospital;

    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String about;

    private String picture;

    private String contactNumber;


}

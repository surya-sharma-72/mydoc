package com.app.mydoc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;


    private String phone;

    private String cabinNumber;

    @Column(nullable = false)
    private String specialization;

    private Integer experience; // years

    @Column(nullable = false)
    private Double fee;

    private String education;

    private String picture; // nullable for now

    // MANY doctors belong to ONE department
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

}

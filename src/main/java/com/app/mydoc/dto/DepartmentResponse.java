package com.app.mydoc.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse {

    private UUID id;
    private String name;
    private String description;
    private UUID hospitalId;
}

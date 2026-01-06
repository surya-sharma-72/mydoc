package com.app.mydoc.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateReceptionistRequest {
    private String email;
    private String password;
    private UUID hospitalId;
}

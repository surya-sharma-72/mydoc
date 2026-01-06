package com.app.mydoc.controller;

import com.app.mydoc.dto.CreateReceptionistRequest;
import com.app.mydoc.dto.LoginRequest;
import com.app.mydoc.dto.LoginResponse;
import com.app.mydoc.dto.SignupRequest;
import com.app.mydoc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // -----------------------
    // USER SIGNUP
    // -----------------------
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {

        userService.signup(request.getEmail(), request.getPassword());
        return ResponseEntity.ok("User created successfully");
    }

    // -----------------------
    // LOGIN → JWT
    // -----------------------
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        String token = userService.authenticateAndGenerateToken(request);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    // -----------------------
    // SUPERADMIN → CREATE ADMIN
    // -----------------------
    @PostMapping("/create-receptionist")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createReceptionist(@RequestBody CreateReceptionistRequest request) {

        userService.createReceptionist(request);
        return ResponseEntity.ok("Hospital receptionist created");
    }
}


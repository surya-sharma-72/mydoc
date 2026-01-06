package com.app.mydoc.service;

import com.app.mydoc.dto.CreateReceptionistRequest;
import com.app.mydoc.dto.LoginRequest;
import com.app.mydoc.entity.Role;
import com.app.mydoc.entity.User;
import com.app.mydoc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // -----------------------
    // USER SIGNUP
    // -----------------------
    public void signup(String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .hospitalId(null)
                .build();

        userRepository.save(user);
    }

    // -----------------------
    // LOGIN → JWT
    // -----------------------
    public String authenticateAndGenerateToken(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(user);
    }

    // -----------------------
    // CREATE HOSPITAL ADMIN
    // -----------------------
    public void createReceptionist(CreateReceptionistRequest request) {

        User receptionist = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.RECEPTIONIST)
                .hospitalId(request.getHospitalId())
                .build();

        userRepository.save(receptionist);
    }
}

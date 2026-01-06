package com.app.mydoc.service;

import com.app.mydoc.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ------------------------
    // Generate JWT
    // ------------------------
    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("role", user.getRole().name())
                .claim("hospitalId",
                        user.getHospitalId() != null ? user.getHospitalId().toString() : null)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ------------------------
    // Extract userId
    // ------------------------
    public UUID extractUserId(String token) {
        return UUID.fromString(extractAllClaims(token).getSubject());
    }

    // ------------------------
    // Extract Role
    // ------------------------
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ------------------------
    // Validate token
    // ------------------------
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    // ------------------------
    // Claims
    // ------------------------
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }


    public UUID extractHospitalId(String token) {
        String hospitalId = extractAllClaims(token).get("hospitalId", String.class);
        return hospitalId != null ? UUID.fromString(hospitalId) : null;
    }

}

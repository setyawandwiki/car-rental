package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.response.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JWTService {
    @Value("${jwt.secret}")
    private String jwtSecret;
    public SecretKey secretKey(){
//        byte[] keyBytes =
    }
    public String extractUsername(String token) {
        return null;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return false;
    }

    public String generateToken(String email) {
        return null;
    }
}

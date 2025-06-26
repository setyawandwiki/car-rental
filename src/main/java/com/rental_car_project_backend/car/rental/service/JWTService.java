package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.response.AuthResponse;
import org.springframework.security.core.userdetails.UserDetails;

public class JWTService {
    public String extractUsername(String token) {
        return null;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return false;
    }

    public AuthResponse generateToken(String email) {
        return null;
    }
}

package com.rental_car_project_backend.car.rental.service;

import org.springframework.security.core.userdetails.UserDetails;

public class JWTService {
    public String extractUsername(String token) {
        return null;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return false;
    }
}

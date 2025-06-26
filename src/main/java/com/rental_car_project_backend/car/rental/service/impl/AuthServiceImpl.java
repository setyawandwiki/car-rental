package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.AuthRequest;
import com.rental_car_project_backend.car.rental.dto.request.UserRequest;
import com.rental_car_project_backend.car.rental.dto.response.AuthResponse;
import com.rental_car_project_backend.car.rental.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse register(UserRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        return null;
    }
}

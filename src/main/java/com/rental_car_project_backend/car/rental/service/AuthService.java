package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.LoginRequest;
import com.rental_car_project_backend.car.rental.dto.request.RegisterRequest;
import com.rental_car_project_backend.car.rental.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}

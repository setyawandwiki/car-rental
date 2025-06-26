package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.LoginRequest;
import com.rental_car_project_backend.car.rental.dto.request.RegisterRequest;
import com.rental_car_project_backend.car.rental.dto.response.LoginResponse;
import com.rental_car_project_backend.car.rental.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}

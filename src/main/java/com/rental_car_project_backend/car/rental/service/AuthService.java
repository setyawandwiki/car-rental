package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.AuthRequest;
import com.rental_car_project_backend.car.rental.dto.request.UserRequest;
import com.rental_car_project_backend.car.rental.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(UserRequest request);
    AuthResponse login(AuthRequest request);
}

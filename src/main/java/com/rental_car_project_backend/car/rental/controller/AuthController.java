package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.LoginRequest;
import com.rental_car_project_backend.car.rental.dto.request.RegisterRequest;
import com.rental_car_project_backend.car.rental.dto.response.LoginResponse;
import com.rental_car_project_backend.car.rental.dto.response.RegisterResponse;
import com.rental_car_project_backend.car.rental.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping(path = "/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request){
        RegisterResponse register = authService.register(request);
        return ResponseEntity.status(HttpStatus.OK).body(register);
    }
    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        LoginResponse login = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(login);
    }
}

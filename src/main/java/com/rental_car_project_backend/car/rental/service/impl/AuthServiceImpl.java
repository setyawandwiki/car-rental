package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.user.LoginRequest;
import com.rental_car_project_backend.car.rental.dto.request.user.RegisterRequest;
import com.rental_car_project_backend.car.rental.dto.response.user.LoginResponse;
import com.rental_car_project_backend.car.rental.dto.response.user.RegisterResponse;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.exceptions.UsernameAndPasswordInvalidException;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import com.rental_car_project_backend.car.rental.service.AuthService;
import com.rental_car_project_backend.car.rental.service.JWTService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JWTService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        Users users = new Users();
        users.setEmail(request.getEmail());
        users.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        users.setIdRole(1);
        Users save = userRepository.save(users);
        return RegisterResponse.builder()
                .id(save.getId())
                .email(save.getEmail())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Users users = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new UsernameNotFoundException("User Not Found"));
        RegisterResponse userResponse = RegisterResponse.builder()
                .email(users.getEmail())
                .id(users.getId())
                .build();
        if(authenticate.isAuthenticated()){
            String token = jwtService.generateToken(request.getEmail());
            return LoginResponse.builder()
                    .message("Login Successful")
                    .token(token)
                    .userResponse(userResponse)
                    .build();
        }else{
            throw new UsernameAndPasswordInvalidException("Invalid email or password");
        }
    }
}

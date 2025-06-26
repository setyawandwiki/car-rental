package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.AuthRequest;
import com.rental_car_project_backend.car.rental.dto.request.UserRequest;
import com.rental_car_project_backend.car.rental.dto.response.AuthResponse;
import com.rental_car_project_backend.car.rental.dto.response.UserResponse;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import com.rental_car_project_backend.car.rental.service.AuthService;
import com.rental_car_project_backend.car.rental.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    public AuthResponse register(UserRequest request) {
        Users users = new Users();
        users.setBirthDate(request.getBirthDate());
        users.setEmail(request.getEmail());
        users.setIdCity(request.getIdCity());
        users.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        users.setIdRole(request.getIdRole());
        users.setFullName(request.getFullName());
        users.setIdCity(request.getIdCity());
        Users save = userRepository.save(users);
        UserResponse userResponse = UserResponse.builder()
                .id(save.getId())
                .email(save.getEmail())
                .birthDate(save.getBirthDate())
                .fullName(save.getFullName())
                .idCity(save.getIdCity())
                .phoneNumber(save.getPhoneNumber())
                .build();
        return AuthResponse.builder()
                .message("User registeres sucessfully")
                .userResponse(userResponse)
                .build();
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        if(authenticate.isAuthenticated()){
            return jwtService.generateToken(request.getEmail());
        }else{
            throw UsernameAndPasswordInvalidException();
        }
    }
}

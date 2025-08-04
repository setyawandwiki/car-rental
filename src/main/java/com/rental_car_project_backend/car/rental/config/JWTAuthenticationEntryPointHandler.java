package com.rental_car_project_backend.car.rental.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental_car_project_backend.car.rental.dto.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class JWTAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        ErrorResponse error = ErrorResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message("Wrong Email And Password !")
                .timestamp(new Date())
                .build();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
}

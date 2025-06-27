package com.rental_car_project_backend.car.rental.global;

import com.rental_car_project_backend.car.rental.dto.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class ExceptionFilterHandler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException | MalformedJwtException | SignatureException |
                IllegalArgumentException | BadCredentialsException | CredentialsExpiredException e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");

            ErrorResponse error = ErrorResponse.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthorized: " + e.getMessage())
                    .timestamp(new Date(System.currentTimeMillis()))
                    .build();

            response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(error));
        }
    }
}

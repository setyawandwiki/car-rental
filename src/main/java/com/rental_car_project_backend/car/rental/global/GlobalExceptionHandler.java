package com.rental_car_project_backend.car.rental.global;

import com.rental_car_project_backend.car.rental.dto.response.ErrorResponse;
import com.rental_car_project_backend.car.rental.exceptions.ExpiredJwtException;
import com.rental_car_project_backend.car.rental.exceptions.NullPointerException;
import com.rental_car_project_backend.car.rental.exceptions.UserNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.UsernameAndPasswordInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userNotFoundException(HttpServletRequest request,
                                               HttpServletResponse response,
                                               UserNotFoundException e){
        log.info("something wrong with endpoint {}, with status code {}, message : {}",
                request.getRequestURI(), response.getStatus(), e.getMessage());
        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(UsernameAndPasswordInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse usernameAndPasswordInvalid(HttpServletRequest request,
                                               HttpServletResponse response,
                                               UsernameAndPasswordInvalidException e){
        log.info("something wrong with endpoint {}, with status code {}, message : {}",
                request.getRequestURI(), response.getStatus(), e.getMessage());
        return ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse nullPointerException(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    NullPointerException e){
        log.info("something wrong with endpoint {}, with status code {}, message : {}",
                request.getRequestURI(), response.getStatus(), e.getMessage());
        return ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse nullPointerException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              ExpiredJwtException e){
        log.info("something wrong with endpoint {}, with status code {}, message : {}",
                request.getRequestURI(), response.getStatus(), e.getMessage());
        return ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}

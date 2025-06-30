package com.rental_car_project_backend.car.rental.global;

import com.rental_car_project_backend.car.rental.dto.response.ErrorResponse;
import com.rental_car_project_backend.car.rental.exceptions.*;
import com.rental_car_project_backend.car.rental.exceptions.NullPointerException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

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
                .timestamp(new Date(System.currentTimeMillis()))
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
                .timestamp(new Date(System.currentTimeMillis()))
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
                .timestamp(new Date(System.currentTimeMillis()))
                .build();
    }
    @ExceptionHandler(CarNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse carNotFoundException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              CarNotFoundException e){
        log.info("something wrong with endpoint {}, with status code {}, message : {}",
                request.getRequestURI(), response.getStatus(), e.getMessage());
        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .timestamp(new Date(System.currentTimeMillis()))
                .build();
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illeGalArguemtnException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              IllegalArgumentException e){
        log.info("something wrong with endpoint {}, with status code {}, message : {}",
                request.getRequestURI(), response.getStatus(), e.getMessage());
        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .timestamp(new Date(System.currentTimeMillis()))
                .build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(HttpServletRequest request,
                                               HttpServletResponse response,
                                               MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.info("Validation failed at {}, status code {}, message: {}",
                request.getRequestURI(), response.getStatus(), errorMessage);

        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed: " + errorMessage)
                .timestamp(new Date(System.currentTimeMillis()))
                .build();
    }
    @ExceptionHandler(CompanyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse companyNotFound(HttpServletRequest request,
                                               HttpServletResponse response,
                                               CompanyNotFoundException e) {
        log.info("something wrong with endpoint {}, with status code {}, message : {}",
                request.getRequestURI(), response.getStatus(), e.getMessage());
        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .timestamp(new Date(System.currentTimeMillis()))
                .build();
    }
}

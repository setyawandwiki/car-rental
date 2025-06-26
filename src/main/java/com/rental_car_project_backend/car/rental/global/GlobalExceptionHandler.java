package com.rental_car_project_backend.car.rental.global;

import com.rental_car_project_backend.car.rental.dto.response.ErrorResponse;
import com.rental_car_project_backend.car.rental.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
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
}

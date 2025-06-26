package com.rental_car_project_backend.car.rental.exceptions;

public class UsernameAndPasswordInvalidException extends RuntimeException {
    public UsernameAndPasswordInvalidException(String message) {
        super(message);
    }
}

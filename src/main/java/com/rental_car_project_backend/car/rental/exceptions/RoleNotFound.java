package com.rental_car_project_backend.car.rental.exceptions;

public class RoleNotFound extends RuntimeException {
    public RoleNotFound(String message) {
        super(message);
    }
}

package com.rental_car_project_backend.car.rental.exceptions;

public class CompanyCarNotFoundException extends RuntimeException {
    public CompanyCarNotFoundException(String message) {
        super(message);
    }
}

package com.rental_car_project_backend.car.rental.exceptions;

public class IncompletableDataException extends RuntimeException {
    public IncompletableDataException(String message) {
        super(message);
    }
}

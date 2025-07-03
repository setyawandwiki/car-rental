package com.rental_car_project_backend.car.rental.exceptions;

public class OrdersNotFoundException extends RuntimeException {
    public OrdersNotFoundException(String message) {
        super(message);
    }
}

package com.rental_car_project_backend.car.rental.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CarRequest {
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "seats is required")
    private Integer seats;
    @NotBlank(message = "baggaes is required")
    private Integer baggages;
    @NotBlank(message = "year is required")
    private Integer year;
    @NotBlank(message = "image is required")
    private String image;
}

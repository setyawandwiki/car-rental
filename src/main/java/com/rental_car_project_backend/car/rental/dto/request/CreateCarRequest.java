package com.rental_car_project_backend.car.rental.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCarRequest {
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

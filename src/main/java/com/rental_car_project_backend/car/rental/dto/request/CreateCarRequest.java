package com.rental_car_project_backend.car.rental.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCarRequest {
    @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "seats is required")
    private Integer seats;
    @NotNull(message = "baggaes is required")
    private Integer baggages;
    @NotNull(message = "year is required")
    private Integer year;
    @NotNull(message = "image is required")
    private String image;
}

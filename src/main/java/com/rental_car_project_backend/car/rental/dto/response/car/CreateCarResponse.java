package com.rental_car_project_backend.car.rental.dto.response.car;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateCarResponse {
    private Integer id;
    private String name;
    private Integer seats;
    private Integer baggages;
    private Integer year;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

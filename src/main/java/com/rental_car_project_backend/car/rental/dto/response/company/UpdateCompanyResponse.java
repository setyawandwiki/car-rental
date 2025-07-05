package com.rental_car_project_backend.car.rental.dto.response.company;

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
public class UpdateCompanyResponse {
    private Integer id;
    private String name;
    private Double rate;
    private String image;
    private Integer idUser;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

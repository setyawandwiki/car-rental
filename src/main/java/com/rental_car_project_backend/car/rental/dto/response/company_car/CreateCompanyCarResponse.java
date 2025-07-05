package com.rental_car_project_backend.car.rental.dto.response.company_car;

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
public class CreateCompanyCarResponse {
    private Integer id;
    private Integer idCompany;
    private Double price;
    private Integer idCar;
    private Integer idCarType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

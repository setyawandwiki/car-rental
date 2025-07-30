package com.rental_car_project_backend.car.rental.dto.response.company_car;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rental_car_project_backend.car.rental.enums.CompanyCarStatus;
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
public class GetCompanyCarResponse {
    private Integer id;
    private Integer idCompany;
    private Double price;
    private Integer idCar;
    private String carType;
    private String city;
    private CompanyCarStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

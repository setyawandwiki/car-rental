package com.rental_car_project_backend.car.rental.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateCompanyCarRequest {
    private Integer CompanyId;
    private Integer CarId;
    private Integer idCompany;
    private Double price;
    private Integer idCarType;
}

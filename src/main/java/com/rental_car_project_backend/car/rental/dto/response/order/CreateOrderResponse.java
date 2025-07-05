package com.rental_car_project_backend.car.rental.dto.response.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rental_car_project_backend.car.rental.dto.response.car.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company.GetCompanyResponse;
import com.rental_car_project_backend.car.rental.enums.OrderStatus;
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
public class CreateOrderResponse {
    private Integer id;
    private String pickUpLoc;
    private String dropOffLoc;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime pickUpDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dropOffDate;
    private double priceTotal;
    private Integer idCompanyCars;
    private Integer idUser;
    private OrderStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;
    private GetCarResponse carResponse;
    private GetCompanyResponse companyResponse;
}

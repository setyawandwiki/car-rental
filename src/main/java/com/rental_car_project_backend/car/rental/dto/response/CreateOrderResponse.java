package com.rental_car_project_backend.car.rental.dto.response;

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
public class CreateOrderResponse {
    private Integer id;
    private String pickupLoc;
    private String dropoff_loc;
    private LocalDateTime pickupDate;
    private LocalDateTime dropoffDate;
    private double priceTotal;
    private Integer idCompany;
    private Integer idUser;
    private Integer idStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedat;
    private GetCarResponse carResponse;
    private GetCompany
}

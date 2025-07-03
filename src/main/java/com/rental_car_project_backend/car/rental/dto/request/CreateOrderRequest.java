package com.rental_car_project_backend.car.rental.dto.request;

import com.rental_car_project_backend.car.rental.dto.response.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetCompanyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    private String pickupLoc;
    private String dropoff_loc;
    private LocalDateTime pickupDate;
    private LocalDateTime dropoffDate;
    private double priceTotal;
    private Integer idCompany;
    private Integer idUser;
    private Integer idStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private GetCarResponse carResponse;
    private GetCompanyResponse companyResponse;
}

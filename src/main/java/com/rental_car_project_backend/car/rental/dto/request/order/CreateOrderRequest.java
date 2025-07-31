package com.rental_car_project_backend.car.rental.dto.request.order;

import com.rental_car_project_backend.car.rental.dto.response.car.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company.GetCompanyResponse;
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
    private String dropOffLoc;
    private LocalDateTime pickupDate;
    private LocalDateTime dropOffDate;
    private double priceTotal;
    private Integer idCompanyCars;
    private Integer idUser;
    private String status;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private GetCarResponse carResponse;
    private GetCompanyResponse companyResponse;
}

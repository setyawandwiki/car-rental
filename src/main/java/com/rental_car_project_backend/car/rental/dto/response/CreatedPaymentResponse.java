package com.rental_car_project_backend.car.rental.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rental_car_project_backend.car.rental.enums.OrderStatus;
import jakarta.persistence.*;
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
public class CreatedPaymentResponse {
    private Integer id;
    private Integer orderId;
    private Integer invoiceId;
    private Integer externalId;
    private OrderStatus orderStatus;
    private Double amount;
    private String paymentUrl;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}

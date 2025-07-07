package com.rental_car_project_backend.car.rental.dto.request.payment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class PaymentNotificationRequest {
    private String id;
    private String externalId;
    private OrderStatus status;
    private Double amount;
    private String currency;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}

package com.rental_car_project_backend.car.rental.dto.response.payment;

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
public class CreatedPaymentResponse {
    private Integer id;
    private Integer orderId;
    private String invoiceId;
    private String externalId;
    private OrderStatus orderStatus;
    private Double amount;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}

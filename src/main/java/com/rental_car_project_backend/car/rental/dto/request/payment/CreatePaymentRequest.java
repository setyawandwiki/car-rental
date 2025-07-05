package com.rental_car_project_backend.car.rental.dto.request.payment;

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
public class CreatePaymentRequest {
    private Integer orderId;
    private Integer invoiceId;
    private Integer externalId;
    private OrderStatus orderStatus;
    private Double amount;
    private String paymentUrl;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}

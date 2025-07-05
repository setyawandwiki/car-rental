package com.rental_car_project_backend.car.rental.entity;

import com.rental_car_project_backend.car.rental.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "payments")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name = "invoice_id")
    private Integer invoiceId;
    @Column(name = "external_id")
    private Integer externalId;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

package com.rental_car_project_backend.car.rental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "platform_profits")
public class PlatFormProfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name = "company_id")
    private Integer companyId;
    @Column(name = "profit_amount")
    private Double profitAmount;
    @Column(name = "percentage")
    private Double percentage;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}

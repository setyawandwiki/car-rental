package com.rental_car_project_backend.car.rental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "vendor_balance")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "company_id")
    private Integer companyId;
    @Column(name = "available_balance")
    private Double availableBalance;
    @Column(name = "pending_withdrawal")
    private Double pendingWithDrawl;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

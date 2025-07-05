package com.rental_car_project_backend.car.rental.entity;

import com.rental_car_project_backend.car.rental.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "orders")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "pickup_loc")
    private String pickupLoc;
    @Column(name = "drop_off_loc")
    private String dropOffLoc;
    @Column(name = "pickup_date")
    private LocalDateTime pickupDate;
    @Column(name = "drop_off_date")
    private LocalDateTime dropOffDate;
    @Column(name = "price_total")
    private double priceTotal;
    @Column(name = "id_company_cars")
    private Integer idCompanyCars;
    @Column(name = "id_user")
    private Integer idUser;
    @Column(name = "id_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

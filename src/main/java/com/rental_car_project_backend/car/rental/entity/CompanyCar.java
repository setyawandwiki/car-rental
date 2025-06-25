package com.rental_car_project_backend.car.rental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "company_cars")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "id_company")
    private int idCompany;
    @Column(name = "price")
    private double price;
    @Column(name = "id_car_type")
    private int idCarType;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

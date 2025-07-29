package com.rental_car_project_backend.car.rental.entity;

import com.rental_car_project_backend.car.rental.enums.CompanyCarStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "company_cars")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "id_company")
    private Integer idCompany;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", insertable = false, updatable = false)
    private Companies company;
    @Column(name = "price")
    private Double price;
    @Column(name = "id_car")
    private Integer idCar;
    @Column(name = "id_car_type")
    private Integer idCarType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_car_type", insertable = false, updatable = false)
    private CarTypes carTypes;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private CompanyCarStatus status;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

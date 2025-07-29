package com.rental_car_project_backend.car.rental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "companies")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Companies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "rate")
    private Double rate;
    @Column(name = "image")
    private String image;
    @Column(name = "id_city")
    private Integer idCity;
    @ManyToOne
    @JoinColumn(name = "id_city", insertable = false, updatable = false)
    private Cities cities;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "id_user")
    private Integer idUser;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

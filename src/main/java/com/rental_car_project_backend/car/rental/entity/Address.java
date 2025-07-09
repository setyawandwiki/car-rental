package com.rental_car_project_backend.car.rental.entity;

import com.rental_car_project_backend.car.rental.enums.AddressStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "address")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "address")
    private String address;
    @Column(name = "id_user")
    private Integer idUSer;
    @Column(name = "id_city")
    private Integer idCity;
    @ManyToOne
    @JoinColumn(name = "id_city", insertable = false, updatable = false)
    private Cities city;
    @ManyToOne
    @JoinColumn(name = "id_user", insertable = false, updatable = false)
    private Users users;
    @Column(name = "status")
    private AddressStatus status;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

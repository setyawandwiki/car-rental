package com.rental_car_project_backend.car.rental.dto.request;

import com.rental_car_project_backend.car.rental.enums.CompanyCarStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCompanyCarRequest {
    private Integer idCompany;
    private Double price;
    private Integer idCar;
    private Integer idCarType;
    private CompanyCarStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

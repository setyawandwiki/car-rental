package com.rental_car_project_backend.car.rental.dto.response.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    private Integer id;
    private String fullName;
    private Integer idCity;
    private Date birthDate;
    private String email;
    private String phoneNumber;
    private String password;
    private String accountNumber;
    private String bankCode;
    private Integer idRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

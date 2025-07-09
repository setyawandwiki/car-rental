package com.rental_car_project_backend.car.rental.dto.response.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rental_car_project_backend.car.rental.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateUserResponse {
    private Integer id;
    private String fullName;
    private Date birthDate;
    private String email;
    private String phoneNumber;
    private String accountNumber;
    private String bankCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

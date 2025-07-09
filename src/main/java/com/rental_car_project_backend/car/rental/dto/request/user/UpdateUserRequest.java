package com.rental_car_project_backend.car.rental.dto.request.user;

import com.rental_car_project_backend.car.rental.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateUserRequest {
    private String fullName;
    private Date birthDate;
    private String phoneNumber;
    private String accountNumber;
    private String bankCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

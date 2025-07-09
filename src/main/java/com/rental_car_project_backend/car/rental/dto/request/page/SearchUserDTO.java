package com.rental_car_project_backend.car.rental.dto.request.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchUserDTO {
    private String fullName;
    private String city;
    private String email;
    private String phoneNumber;
    private String accountNumber;
    private String bankCode;
}

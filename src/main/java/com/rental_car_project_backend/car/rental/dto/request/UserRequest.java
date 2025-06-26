package com.rental_car_project_backend.car.rental.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "full name cannot be null")
    private String fullName;
    @NotBlank(message = "city cannot be null")
    private int idCity;
    @NotBlank(message = "birth date cannot be null")
    private Date birthDate;
    @NotBlank(message = "email cannot be null")
    @Email
    private String email;
    @NotBlank(message = "phone number cannot be null")
    @Pattern(regexp = "^(\\+62|62|0)8[1-9][0-9]{6,9}$")
    private String phoneNumber;
    @NotBlank(message = "password cannot be null")
    private String password;
    private int idRole;
}

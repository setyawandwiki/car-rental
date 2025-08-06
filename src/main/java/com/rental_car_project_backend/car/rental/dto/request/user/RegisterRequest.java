package com.rental_car_project_backend.car.rental.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "email cannot be null")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "password cannot be null")
    @Size(min = 5, message = "Password mininum 5 character")
    private String password;
}

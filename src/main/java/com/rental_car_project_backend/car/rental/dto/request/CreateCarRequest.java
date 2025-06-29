package com.rental_car_project_backend.car.rental.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCarRequest {
    @NotNull(message = "name is required")
    @Size(min = 3, max = 100, message = "min name 3 character and max name 100 character")
    private String name;
    @NotNull(message = "seats is required")
    private Integer seats;
    @NotNull(message = "baggaes is required")
    private Integer baggages;
    @NotNull(message = "year is required")
    private Integer year;
    @NotNull(message = "image is required")
    private MultipartFile imageFile;
}

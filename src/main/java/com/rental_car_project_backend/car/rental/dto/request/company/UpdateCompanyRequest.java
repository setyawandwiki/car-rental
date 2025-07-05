package com.rental_car_project_backend.car.rental.dto.request.company;

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
public class UpdateCompanyRequest {
    @NotNull(message = "name is required")
    @Size(min = 3, max = 100, message = "min name 3 character and max name 100 character")
    private String name;
    private Double rate = 3.5;
    private MultipartFile imageFile;
}

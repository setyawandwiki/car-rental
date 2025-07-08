package com.rental_car_project_backend.car.rental.dto.request.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchRequestDTO {
    private String value;
}

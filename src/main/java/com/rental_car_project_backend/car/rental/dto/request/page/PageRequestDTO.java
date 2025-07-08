package com.rental_car_project_backend.car.rental.dto.request.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestDTO {
    private String pageNo = "0";
    private String pageSize = "10";
    private Sort.Direction sort = Sort.Direction.ASC;
}

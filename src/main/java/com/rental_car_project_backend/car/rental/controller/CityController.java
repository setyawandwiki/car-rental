package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.car.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.city.GetCitiesResponse;
import com.rental_car_project_backend.car.rental.service.CityService;
import com.rental_car_project_backend.car.rental.service.impl.CityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/city")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;
    @GetMapping
    public ResponseEntity<Page<GetCitiesResponse>> getAllCars(
            @RequestParam(value = "name", defaultValue =  "", required = false) String value,
            @RequestParam(value = "page", required = false, defaultValue = "0") String page,
            @RequestParam(value = "sort", defaultValue = "ASC", required = false) Sort.Direction sort,
            @RequestParam(value = "size", required = false, defaultValue = "10") String size
    ) {
        SearchRequestDTO requestDTO = SearchRequestDTO.builder()
                .value(value)
                .build();
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .pageNo(page)
                .pageSize(size)
                .sort(sort)
                .sortColumn("id")
                .build();
        Page<GetCitiesResponse> cities = cityService.getCities(requestDTO, pageRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(cities);
    }
}

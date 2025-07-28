package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.city.GetCitiesResponse;
import com.rental_car_project_backend.car.rental.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CityServiceImpl implements CityService {
    @Override
    public Page<GetCitiesResponse> getCities(SearchRequestDTO requestDTO, PageRequestDTO pageRequestDTO) {
        return null;
    }
}

package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.car.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.city.GetCitiesResponse;
import org.springframework.data.domain.Page;

public interface CityService {
    Page<GetCitiesResponse> getCities(SearchRequestDTO requestDTO, PageRequestDTO pageRequestDTO);
}

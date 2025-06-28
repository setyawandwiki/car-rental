package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.CreateCarRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetCarResponse;

public interface CarService {
    CreateCarResponse create(CreateCarRequest request);
    GetCarResponse getCar(Integer id);
}

package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.CreateCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.UpdateCarRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.DeleteCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.UpdateCarResponse;

import java.io.IOException;
import java.util.List;

public interface CarService {
    CreateCarResponse create(CreateCarRequest request) throws IOException;
    GetCarResponse getCar(Integer id);
    List<GetCarResponse> getCars();
    DeleteCarResponse deleteCarById(Integer id) throws IOException;
    UpdateCarResponse updateCar(Integer id, UpdateCarRequest request) throws IOException;
}

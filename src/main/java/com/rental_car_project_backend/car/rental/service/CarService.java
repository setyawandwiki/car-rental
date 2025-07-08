package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.car.CreateCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.car.UpdateCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.car.CreateCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.car.DeleteCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.car.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.car.UpdateCarResponse;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface CarService {
    CreateCarResponse create(CreateCarRequest request) throws IOException;
    GetCarResponse getCar(Integer id);
    Page<GetCarResponse> getCars(SearchRequestDTO requestDTO, PageRequestDTO pageRequestDTO);
    DeleteCarResponse deleteCarById(Integer id) throws IOException;
    UpdateCarResponse updateCar(Integer id, UpdateCarRequest request) throws IOException;
}

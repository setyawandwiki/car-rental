package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCompanyCarResponse;

public interface CompanyCarService {
    CreateCompanyCarResponse createCompanyCar(CreateCompanyCarRequest request);
}

package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.request.UpdateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCompanyResponse;
import com.rental_car_project_backend.car.rental.dto.response.UpdateCompanyResponse;

import java.io.IOException;

public interface CompanyService {
    CreateCompanyResponse createCompany(CreateCompanyRequest request) throws IOException;
    UpdateCompanyResponse updateCompanyResponse(Integer id, UpdateCompanyRequest request) throws IOException;
}

package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.request.UpdateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.DeleteCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.UpdateCompanyCarResponse;

import java.util.List;

public interface CompanyCarService {
    CreateCompanyCarResponse createCompanyCar(CreateCompanyCarRequest request);
    List<GetCompanyCarResponse> getCompanyCars();
    DeleteCompanyCarResponse deleteCompanyCar(Integer id);
    UpdateCompanyCarResponse updateCompanyCar(Integer id, UpdateCompanyCarRequest request);
}

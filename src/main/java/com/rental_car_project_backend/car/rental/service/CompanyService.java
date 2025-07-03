package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.request.UpdateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.response.*;

import java.io.IOException;
import java.util.List;

public interface CompanyService {
    CreateCompanyResponse createCompany(CreateCompanyRequest request) throws IOException;
    UpdateCompanyResponse updateCompany(Integer id, UpdateCompanyRequest request) throws IOException;
    DeleteCompanyResponse deletecompany(Integer id) throws IOException;
    List<GetCompanyResponse> getCompanies();
    GetCompanyResponse findCompany(Integer id);
}

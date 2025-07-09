package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.company_car.CreateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.company_car.UpdateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.page.CompanyCarSearchRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.company_car.CreateCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.DeleteCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.GetCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.UpdateCompanyCarResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CompanyCarService {
    CreateCompanyCarResponse createCompanyCar(CreateCompanyCarRequest request);
    Page<GetCompanyCarResponse> getCompanyCars(
                                               PageRequestDTO pageRequestDTO);
    DeleteCompanyCarResponse deleteCompanyCar(Integer id);
    UpdateCompanyCarResponse updateCompanyCar(Integer id, UpdateCompanyCarRequest request);
    GetCompanyCarResponse findCompanyCar(Integer id);
}

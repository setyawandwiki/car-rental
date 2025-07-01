package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCompanyCarResponse;
import com.rental_car_project_backend.car.rental.service.CompanyCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyCarServiceImpl implements CompanyCarService {
    @Override
    public CreateCompanyCarResponse createCompanyCar(CreateCompanyRequest request) {
        return null;
    }
}

package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCompanyCarResponse;
import com.rental_car_project_backend.car.rental.repository.CompanyCarRepository;
import com.rental_car_project_backend.car.rental.service.CompanyCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyCarServiceImpl implements CompanyCarService {
    private final CompanyCarRepository companyCarRepository;
    @Override
    public CreateCompanyCarResponse createCompanyCar(CreateCompanyRequest request) {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        return null;
    }
}

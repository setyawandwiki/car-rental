package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCompanyCarResponse;
import com.rental_car_project_backend.car.rental.entity.Cars;
import com.rental_car_project_backend.car.rental.entity.Companies;
import com.rental_car_project_backend.car.rental.entity.CompanyCar;
import com.rental_car_project_backend.car.rental.exceptions.CarNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.CompanyNotFoundException;
import com.rental_car_project_backend.car.rental.repository.CarRepository;
import com.rental_car_project_backend.car.rental.repository.CompanyCarRepository;
import com.rental_car_project_backend.car.rental.repository.CompanyRepository;
import com.rental_car_project_backend.car.rental.service.CompanyCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CompanyCarServiceImpl implements CompanyCarService {
    private final CompanyCarRepository companyCarRepository;
    private final CompanyRepository companyRepository;
    private final CarRepository carRepository;
    @Override
    public CreateCompanyCarResponse createCompanyCar(CreateCompanyCarRequest request) {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        Companies company = companyRepository.findById(request.getCompanyId()).orElseThrow(() ->
                new CompanyNotFoundException("Company not foound with id " + request.getIdCompany()));
        Cars car = carRepository.findById(request.getCarId()).orElseThrow(() ->
                new CarNotFoundException("Car not found with id " + request.getCarId()));
        CompanyCar companyCar = new CompanyCar();
        companyCar.setIdCompany(companyCar.getId());
        companyCar.setIdCar(companyCar.getIdCar());
        companyCar.setIdCarType(request.getIdCarType());
        companyCar.setCreatedAt(LocalDateTime.now());
        companyCar.setPrice(request.getPrice());
        CompanyCar save = companyCarRepository.save(companyCar);
        return CreateCompanyCarResponse.builder()
                .id(save.getId())
                .createdAt(save.getCreatedAt())
                .updatedAt(save.getUpdatedAt())
                .idCarType(save.getIdCarType())
                .idCar(save.getIdCar())
                .idCompany(save.getIdCompany())
                .price(save.getPrice())
                .build();
    }
}

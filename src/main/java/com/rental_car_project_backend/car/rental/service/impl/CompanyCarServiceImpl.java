package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.UpdateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.DeleteCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.UpdateCompanyCarResponse;
import com.rental_car_project_backend.car.rental.entity.Cars;
import com.rental_car_project_backend.car.rental.entity.Companies;
import com.rental_car_project_backend.car.rental.entity.CompanyCar;
import com.rental_car_project_backend.car.rental.enums.CompanyCarStatus;
import com.rental_car_project_backend.car.rental.exceptions.CarNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.CompanyCarNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.CompanyNotFoundException;
import com.rental_car_project_backend.car.rental.repository.CarRepository;
import com.rental_car_project_backend.car.rental.repository.CompanyCarRepository;
import com.rental_car_project_backend.car.rental.repository.CompanyRepository;
import com.rental_car_project_backend.car.rental.service.CompanyCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
        companyCar.setIdCompany(company.getId());
        companyCar.setIdCar(car.getId());
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

    @Override
    public List<GetCompanyCarResponse> getCompanyCars() {
        List<CompanyCar> all = companyCarRepository.findAll();
        return all.stream().map(val -> {
            GetCompanyCarResponse response = new GetCompanyCarResponse();
            response.setId(val.getId());
            response.setPrice(val.getPrice());
            response.setIdCompany(val.getIdCompany());
            response.setIdCarType(val.getIdCarType());
            response.setIdCar(val.getIdCar());
            response.setCreatedAt(val.getCreatedAt());
            response.setStatus(val.getStatus());
            response.setUpdatedAt(val.getUpdatedAt());
            return response;
        }).toList();
    }

    @Override
    public DeleteCompanyCarResponse deleteCompanyCar(Integer id) {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        CompanyCar companyCar = companyCarRepository.findById(id).orElseThrow(()
                -> new CompanyCarNotFoundException("Company car with id " + id + " not found"));

        companyCar.setStatus(CompanyCarStatus.DELETE);
        companyCarRepository.save(companyCar);
        return DeleteCompanyCarResponse.builder()
                .id(companyCar.getId())
                .message("Success delete company car")
                .build();
    }

    @Override
    public UpdateCompanyCarResponse updateCompanyCar(Integer id, UpdateCompanyCarRequest request) {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        CompanyCar companyCar = companyCarRepository.findById(id).orElseThrow(()
                -> new CompanyCarNotFoundException("Company car with id " + id + " not found"));
        if(Objects.nonNull(request.getIdCompany())){
            companyCar.setIdCompany(request.getIdCompany());
        }
        if(Objects.nonNull(request.getIdCar())){
            companyCar.setIdCar(request.getIdCar());
        }
        if(Objects.nonNull(request.getIdCarType())){
            companyCar.setIdCarType(request.getIdCarType());
        }
        if(Objects.nonNull(request.getStatus())){
            companyCar.setStatus(request.getStatus());
        }
        if(Objects.nonNull(request.getPrice())){
            companyCar.setPrice(request.getPrice());
        }
        companyCar.setUpdatedAt(LocalDateTime.now());
        companyCar.setCreatedAt(companyCar.getCreatedAt());
        CompanyCar save = companyCarRepository.save(companyCar);
        return UpdateCompanyCarResponse.builder()
                .id(save.getId())
                .idCompany(save.getIdCompany())
                .updatedAt(save.getUpdatedAt())
                .idCar(save.getIdCar())
                .createdAt(save.getCreatedAt())
                .idCarType(save.getIdCarType())
                .price(save.getPrice())
                .status(save.getStatus())
                .build();
    }

    @Override
    public GetCompanyCarResponse findCompanyCar(Integer id) {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        CompanyCar companyCar = companyCarRepository.findById(id).orElseThrow(()
                -> new CompanyCarNotFoundException("Company car with id " + id + " not found"));

        return GetCompanyCarResponse.builder()
                .id(companyCar.getId())
                .idCompany(companyCar.getIdCompany())
                .idCar(companyCar.getIdCar())
                .createdAt(companyCar.getCreatedAt())
                .updatedAt(companyCar.getUpdatedAt())
                .idCarType(companyCar.getIdCarType())
                .price(companyCar.getPrice())
                .status(companyCar.getStatus())
                .build();
    }
    }
}

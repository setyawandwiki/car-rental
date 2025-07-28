package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.company_car.CreateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.company_car.UpdateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.company_car.CreateCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.DeleteCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.GetCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.UpdateCompanyCarResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CompanyCarServiceImpl implements CompanyCarService {
    private final CompanyCarRepository companyCarRepository;
    private final CompanyRepository companyRepository;
    private final CarRepository carRepository;
    @Override
    @Transactional
    public CreateCompanyCarResponse createCompanyCar(CreateCompanyCarRequest request) {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        Companies company = companyRepository.findById(request.getIdCompany()).orElseThrow(() ->
                new CompanyNotFoundException("Company not foound with id " + request.getIdCompany()));
        Cars car = carRepository.findById(request.getIdCar()).orElseThrow(() ->
                new CarNotFoundException("Car not found with id " + request.getIdCar()));
        CompanyCar companyCar = new CompanyCar();
        companyCar.setIdCompany(company.getId());
        companyCar.setIdCar(car.getId());
        companyCar.setPrice(request.getPrice());
        companyCar.setStatus(CompanyCarStatus.ACTIVE);
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
    public Page<GetCompanyCarResponse> getCompanyCars(PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by(pageRequestDTO.getSort(), "created_at");
        Pageable pageRequest = PageRequest.of(Integer.parseInt(pageRequestDTO.getPageNo()),
                Integer.parseInt(pageRequestDTO.getPageSize()), sort);
        Page<CompanyCar> all = companyCarRepository.findAll(pageRequest);
        return all.map(val -> {
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
        });
    }

    @Override
    @Transactional
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
    @Transactional
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

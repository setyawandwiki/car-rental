package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.company_car.CreateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.company_car.UpdateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.company_car.CreateCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.DeleteCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.GetCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.UpdateCompanyCarResponse;
import com.rental_car_project_backend.car.rental.entity.*;
import com.rental_car_project_backend.car.rental.enums.CompanyCarStatus;
import com.rental_car_project_backend.car.rental.exceptions.CarNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.CompanyCarNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.CompanyNotFoundException;
import com.rental_car_project_backend.car.rental.repository.*;
import com.rental_car_project_backend.car.rental.service.CompanyCarService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CompanyCarServiceImpl implements CompanyCarService {
    private final CompanyCarRepository companyCarRepository;
    private final CompanyRepository companyRepository;
    private final CarRepository carRepository;
    private final CityRepository cityRepository;
    private final CarTypeRepository carTypeRepository;

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
    public Page<GetCompanyCarResponse> getCompanyCars(SearchRequestDTO requestDTO, PageRequestDTO pageRequestDTO) {
        Specification<CompanyCar> carsSpecification = (root,
                                                 query,
                                                 cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<CompanyCar, Companies> companyJoin = root.join("company");
            Join<CompanyCar, CarTypes> carTypeJoin = root.join("carTypes");
            Join<Companies, Cities> cityJoin = companyJoin.join("cities");

            if (Objects.nonNull(requestDTO.getValue())) {
                predicates.add(
                        cb.like(
                                cb.lower(cityJoin.get("name")),
                                "%" + requestDTO.getValue().toLowerCase() + "%"
                        )
                );
            }

            if (Objects.nonNull(requestDTO.getType())) {
                predicates.add(
                        cb.equal(
                                cb.lower(carTypeJoin.get("name")),
                                requestDTO.getType().toLowerCase()
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(pageRequestDTO.getSort(), "id");
        Pageable pageRequest = PageRequest.of(Integer.parseInt(pageRequestDTO.getPageNo()),
                Integer.parseInt(pageRequestDTO.getPageSize()), sort);
        Page<CompanyCar> all = companyCarRepository.findAll(carsSpecification, pageRequest);
        return all.map(val -> {
            GetCompanyCarResponse response = new GetCompanyCarResponse();
            Companies companies = companyRepository.findById(val.getIdCompany()).get();
            Cities cities = cityRepository.findById(companies.getIdCity()).get();
            CompanyCar companyCar = companyCarRepository.findById(val.getId()).get();
            CarTypes carTypes = carTypeRepository.findById(companyCar.getIdCarType()).get();
            Cars cars = carRepository.findById(companyCar.getIdCar()).get();
            response.setId(val.getId());
            response.setPrice(val.getPrice());
            response.setIdCompany(val.getIdCompany());
            response.setCarType(carTypes.getName());
            response.setCar(cars.getName());
            response.setIdCar(val.getIdCar());
            response.setCity(cities.getName());
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
        CarTypes carTypes = carTypeRepository.findById(companyCar.getIdCarType()).get();
        Cars cars = carRepository.findById(companyCar.getIdCar()).get();
        return GetCompanyCarResponse.builder()
                .id(companyCar.getId())
                .idCompany(companyCar.getIdCompany())
                .car(cars.getName())
                .idCar(companyCar.getIdCar())
                .createdAt(companyCar.getCreatedAt())
                .updatedAt(companyCar.getUpdatedAt())
                .carType(carTypes.getName())
                .price(companyCar.getPrice())
                .status(companyCar.getStatus())
                .build();
    }
}

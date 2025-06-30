package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.request.UpdateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCompanyResponse;
import com.rental_car_project_backend.car.rental.dto.response.UpdateCompanyResponse;
import com.rental_car_project_backend.car.rental.entity.Companies;
import com.rental_car_project_backend.car.rental.exceptions.CompanyNotFoundException;
import com.rental_car_project_backend.car.rental.repository.CompanyRepository;
import com.rental_car_project_backend.car.rental.service.CompanyService;
import com.rental_car_project_backend.car.rental.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final ImageUploadService imageUploadService;
    @Override
    public CreateCompanyResponse createCompany(CreateCompanyRequest request) throws IOException {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        String uniqueCompanyId = "company-" + UUID.randomUUID();
        String upload = imageUploadService
                .uploadImage(request.getImageFile(), uniqueCompanyId, "company-images");
        Companies companies = new Companies();
        companies.setCreatedAt(LocalDateTime.now());
        companies.setRate(request.getRate());
        companies.setName(request.getName());
        companies.setImage(upload);
        Companies save = companyRepository.save(companies);
        return CreateCompanyResponse.builder()
                .id(save.getId())
                .name(save.getName())
                .rate(save.getRate())
                .image(save.getImage())
                .createdAt(save.getCreatedAt())
                .updatedAt(save.getUpdatedAt())
                .build();
    }

    @Override
    public UpdateCompanyResponse updateCompanyResponse(Integer id, UpdateCompanyRequest request) throws IOException {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        Companies company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id " + id));
        if(Objects.nonNull(request.getName())){
            company.setName(request.getName());
        }
        if(Objects.nonNull(request.getRate())){
            company.setRate(request.getRate());
        }
        if(Objects.nonNull(request.getImageFile())){
            String publicId = "company-" + id;
            String imageUrl = imageUploadService
                    .uploadImage(request.getImageFile(), publicId, "company-images");
            company.setImage(imageUrl);
        }
        company.setUpdatedAt(LocalDateTime.now());
        company.setCreatedAt(company.getCreatedAt());
        Companies save = companyRepository.save(company);
        return UpdateCompanyResponse.builder()
                .rate(save.getRate())
                .image(save.getImage())
                .name(save.getName())
                .updatedAt(save.getUpdatedAt())
                .createdAt(save.getCreatedAt())
                .build();
    }
}

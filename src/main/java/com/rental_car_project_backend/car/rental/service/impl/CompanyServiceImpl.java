package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCompanyResponse;
import com.rental_car_project_backend.car.rental.entity.Companies;
import com.rental_car_project_backend.car.rental.repository.CompanyRepository;
import com.rental_car_project_backend.car.rental.service.CompanyService;
import com.rental_car_project_backend.car.rental.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
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
}

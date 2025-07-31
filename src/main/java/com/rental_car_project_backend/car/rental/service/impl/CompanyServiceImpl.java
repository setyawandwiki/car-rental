package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.company.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.request.company.UpdateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.response.company.CreateCompanyResponse;
import com.rental_car_project_backend.car.rental.dto.response.company.DeleteCompanyResponse;
import com.rental_car_project_backend.car.rental.dto.response.company.GetCompanyResponse;
import com.rental_car_project_backend.car.rental.dto.response.company.UpdateCompanyResponse;
import com.rental_car_project_backend.car.rental.entity.Cities;
import com.rental_car_project_backend.car.rental.entity.Companies;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.entity.Vendor;
import com.rental_car_project_backend.car.rental.exceptions.CompanyNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.UserNotFoundException;
import com.rental_car_project_backend.car.rental.repository.CityRepository;
import com.rental_car_project_backend.car.rental.repository.CompanyRepository;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import com.rental_car_project_backend.car.rental.repository.VendorRepository;
import com.rental_car_project_backend.car.rental.service.CompanyService;
import com.rental_car_project_backend.car.rental.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final ImageUploadService imageUploadService;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final CityRepository cityRepository;

    @Override
    @Transactional
    public CreateCompanyResponse createCompany(CreateCompanyRequest request) throws IOException {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("user not found with email " + email));
        String uniqueCompanyId = "company-" + UUID.randomUUID();
        String upload = imageUploadService
                .uploadImage(request.getImageFile(), uniqueCompanyId, "company-images");
        Companies companies = new Companies();
        companies.setCreatedAt(LocalDateTime.now());
        companies.setRate(request.getRate());
        companies.setName(request.getName());
        companies.setIdUser(users.getId());
        companies.setIdCity(request.getIdCity());
        companies.setImage(upload);
        Companies save = companyRepository.save(companies);

        Vendor vendor = new Vendor();
        vendor.setPendingWithDrawl(0.0);
        vendor.setCompanyId(save.getId());
        vendor.setAvailableBalance(0.0);
        vendorRepository.save(vendor);

        return CreateCompanyResponse.builder()
                .id(save.getId())
                .name(save.getName())
                .rate(save.getRate())
                .image(save.getImage())
                .idUser(save.getIdUser())
                .createdAt(save.getCreatedAt())
                .updatedAt(save.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public UpdateCompanyResponse updateCompany(Integer id, UpdateCompanyRequest request) throws IOException {
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
            String urlImage = company.getImage();
            String fileName = urlImage.substring(urlImage.lastIndexOf("/") + 1);
            String publicId = fileName.substring(0, fileName.lastIndexOf("."));
            String imageUrl = imageUploadService
                    .uploadImage(request.getImageFile(), publicId, "company-images");
            company.setImage(imageUrl);
        }
        company.setUpdatedAt(LocalDateTime.now());
        company.setCreatedAt(company.getCreatedAt());
        Companies save = companyRepository.save(company);
        return UpdateCompanyResponse.builder()
                .id(company.getId())
                .rate(save.getRate())
                .image(save.getImage())
                .name(save.getName())
                .idUser(save.getIdUser())
                .updatedAt(save.getUpdatedAt())
                .createdAt(save.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public DeleteCompanyResponse deletecompany(Integer id) throws IOException {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        Companies company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id " + id));
        String urlImage = company.getImage();
        String fileName = urlImage.substring(urlImage.lastIndexOf("/") + 1);
        String publicId = fileName.substring(0, fileName.lastIndexOf("."));
        imageUploadService.deleteImage(publicId, "company-images");
        companyRepository.delete(company);
        return DeleteCompanyResponse.builder()
                .id(company.getId())
                .message("Success delete company with id " + id)
                .build();
    }

    @Override
    public List<GetCompanyResponse> getCompanies() {
        List<Companies> companies = companyRepository.findAll();
        return companies.stream().map(val ->{
            GetCompanyResponse companyResponse = new GetCompanyResponse();
            companyResponse.setId(val.getId());
            companyResponse.setRate(val.getRate());
            companyResponse.setName(val.getName());
            companyResponse.setImage(val.getImage());
            companyResponse.setIdUser(val.getIdUser());
            companyResponse.setIdCity(val.getIdCity());
            companyResponse.setCreatedAt(val.getCreatedAt());
            companyResponse.setUpdatedAt(val.getUpdatedAt());
            return companyResponse;
        }).toList();
    }

    @Override
    public GetCompanyResponse findCompany(Integer id) {
        Companies companies = companyRepository.findById(id).orElseThrow(() ->
                new CompanyNotFoundException("Company not found with id " + id));
        Cities cities = cityRepository.findById(companies.getIdCity()).get();
        return GetCompanyResponse.builder()
                .createdAt(companies.getCreatedAt())
                .id(companies.getId())
                .image(companies.getImage())
                .idCity(cities.getId())
                .name(companies.getName())
                .idUser(companies.getIdUser())
                .updatedAt(companies.getUpdatedAt())
                .createdAt(companies.getCreatedAt())
                .build();
    }
}

package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCompanyResponse;
import com.rental_car_project_backend.car.rental.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    @PostMapping
    public ResponseEntity<CreateCompanyResponse> createCompany(@Valid @RequestParam("name") String name,
                                                               @RequestParam("image")MultipartFile image,
                                                               @RequestParam("rate") Double rate) throws IOException {
        CreateCompanyRequest request = new CreateCompanyRequest(name, rate, image);
        CreateCompanyResponse company = companyService.createCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }
}

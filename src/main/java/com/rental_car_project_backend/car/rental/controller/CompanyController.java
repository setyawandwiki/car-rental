package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.request.UpdateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCompanyResponse;
import com.rental_car_project_backend.car.rental.dto.response.UpdateCompanyResponse;
import com.rental_car_project_backend.car.rental.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
                                                               @RequestParam(value = "rate", required = false, defaultValue = "3.5") Double rate) throws IOException {
        CreateCompanyRequest request = new CreateCompanyRequest(name, rate, image);
        CreateCompanyResponse company = companyService.createCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<UpdateCompanyResponse> updateCompany(@PathVariable("id") Integer id,
                                                               @Valid @RequestParam(value = "name", required = false)
                                                               String name,
                                                               @RequestParam(value = "image", required = false)
                                                                   MultipartFile image,
                                                               @RequestParam(value = "rate", required = false)
                                                                   Double rate
    )
            throws IOException {
        UpdateCompanyRequest request = new UpdateCompanyRequest(name, rate, image);
        UpdateCompanyResponse updateCompanyResponse = companyService.updateCompanyResponse(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updateCompanyResponse);
    }
}

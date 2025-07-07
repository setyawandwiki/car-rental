package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.company.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.request.company.UpdateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.response.company.CreateCompanyResponse;
import com.rental_car_project_backend.car.rental.dto.response.company.DeleteCompanyResponse;
import com.rental_car_project_backend.car.rental.dto.response.company.GetCompanyResponse;
import com.rental_car_project_backend.car.rental.dto.response.company.UpdateCompanyResponse;
import com.rental_car_project_backend.car.rental.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    @PostMapping
    public ResponseEntity<CreateCompanyResponse> createCompany(@Valid @RequestParam("name") String name,
                                                               @RequestParam("image")MultipartFile image,
                                                               @RequestParam(value = "rate", required = false, defaultValue = "3.5") Double rate) throws IOException {
        CreateCompanyRequest request = CreateCompanyRequest.builder()
                .imageFile(image)
                .name(name)
                .rate(rate)
                .build();
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
        UpdateCompanyResponse updateCompanyResponse = companyService.updateCompany(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updateCompanyResponse);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DeleteCompanyResponse> deleteCompany(@PathVariable Integer id) throws IOException {
        DeleteCompanyResponse deletecompany = companyService.deletecompany(id);
        return ResponseEntity.ok().body(deletecompany);
    }
    @GetMapping
    public ResponseEntity<List<GetCompanyResponse>> getCompany(){
        List<GetCompanyResponse> companies = companyService.getCompanies();
        return ResponseEntity.ok().body(companies);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<GetCompanyResponse> getCompany(@PathVariable("id") Integer id){
        GetCompanyResponse companies = companyService.findCompany(id);
        return ResponseEntity.ok().body(companies);
    }
}

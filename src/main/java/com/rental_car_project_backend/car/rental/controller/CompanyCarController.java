package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.company_car.CreateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.company_car.UpdateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.company_car.CreateCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.DeleteCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.GetCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.UpdateCompanyCarResponse;
import com.rental_car_project_backend.car.rental.service.CompanyCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/company-car")
@RequiredArgsConstructor
public class CompanyCarController {
    private final CompanyCarService companyCarService;
    @GetMapping
    public ResponseEntity<Page<GetCompanyCarResponse>> getAllCompanyCars(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) String pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) String pageSize){
        PageRequestDTO build = PageRequestDTO.builder()
                .pageSize(pageSize)
                .pageNo(pageNo)
                .build();
        Page<GetCompanyCarResponse> companyCars = companyCarService.getCompanyCars(build);
        return ResponseEntity.ok().body(companyCars);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<GetCompanyCarResponse> getCompanyCar(@PathVariable("id") Integer id){
        GetCompanyCarResponse companyCar = companyCarService.findCompanyCar(id);
        return ResponseEntity.ok().body(companyCar);
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<UpdateCompanyCarResponse> updateCompanyCar(@PathVariable("id") Integer id,
                                                                     @RequestBody UpdateCompanyCarRequest request){
        UpdateCompanyCarResponse updateCompanyCarResponse = companyCarService.updateCompanyCar(id, request);
        return ResponseEntity.ok().body(updateCompanyCarResponse);
    }
    @PostMapping
    public ResponseEntity<CreateCompanyCarResponse> createCompany(@RequestBody CreateCompanyCarRequest request){
        CreateCompanyCarResponse companyCar = companyCarService.createCompanyCar(request);
        return ResponseEntity.ok().body(companyCar);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DeleteCompanyCarResponse> deleteCompany(@PathVariable("id") Integer id){
        DeleteCompanyCarResponse deleteCompanyCarResponse = companyCarService.deleteCompanyCar(id);
        return ResponseEntity.ok().body(deleteCompanyCarResponse);
    }
}

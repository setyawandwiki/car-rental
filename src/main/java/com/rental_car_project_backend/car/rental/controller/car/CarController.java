package com.rental_car_project_backend.car.rental.controller.car;

import com.rental_car_project_backend.car.rental.dto.request.CreateCarRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetCarResponse;
import com.rental_car_project_backend.car.rental.service.impl.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/car")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    @PostMapping
    public ResponseEntity<CreateCarResponse> createCar(@Valid @RequestBody CreateCarRequest request){
        CreateCarResponse createCarResponse = carService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCarResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<GetCarResponse> getCar(@PathVariable("id") Integer id){
        GetCarResponse carResponse = carService.getCar(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(carResponse);
    }
}

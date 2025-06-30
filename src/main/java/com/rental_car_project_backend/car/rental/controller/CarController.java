package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.CreateCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.UpdateCarRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.DeleteCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.UpdateCarResponse;
import com.rental_car_project_backend.car.rental.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/car")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    @PostMapping
    public ResponseEntity<CreateCarResponse> createCar(@Valid @RequestParam("name") String name,
                                                       @RequestParam("year") Integer year,
                                                       @RequestParam("seats") Integer seats,
                                                       @RequestParam("baggages") Integer baggages,
                                                       @RequestParam("image")MultipartFile image) throws IOException {
        CreateCarRequest request = new CreateCarRequest(name, seats, baggages, year, image);
        CreateCarResponse createCarResponse = carService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCarResponse);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<GetCarResponse> getCar(@PathVariable("id") Integer id){
        GetCarResponse carResponse = carService.getCar(id);
        return ResponseEntity.status(HttpStatus.OK).body(carResponse);
    }
    @GetMapping
    public ResponseEntity<List<GetCarResponse>> getAllCars(){
        List<GetCarResponse> cars = carService.getCars();
        return ResponseEntity.status(HttpStatus.OK).body(cars);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DeleteCarResponse> deleteCar(@PathVariable("id") Integer id) throws IOException {
        DeleteCarResponse deleteCarResponse = carService.deleteCarById(id);
        return ResponseEntity.status(HttpStatus.OK).body(deleteCarResponse);
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<UpdateCarResponse> updateCar(@PathVariable("id") Integer id,
                                                       @Valid @RequestParam("name") String name,
                                                       @RequestParam("year") Integer year,
                                                       @RequestParam("seats") Integer seats,
                                                       @RequestParam("baggages") Integer baggages,
                                                       @RequestParam("image")MultipartFile image) throws IOException {
        UpdateCarRequest request = new UpdateCarRequest(name, seats, baggages, year, image);
        UpdateCarResponse updateCarResponse = carService.updateCar(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updateCarResponse);
    }
}

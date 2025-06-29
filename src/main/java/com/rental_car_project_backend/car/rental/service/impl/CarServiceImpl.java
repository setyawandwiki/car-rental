package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.CreateCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.UpdateCarRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.DeleteCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.UpdateCarResponse;
import com.rental_car_project_backend.car.rental.entity.Cars;
import com.rental_car_project_backend.car.rental.exceptions.CarNotFoundException;
import com.rental_car_project_backend.car.rental.repository.CarRepository;
import com.rental_car_project_backend.car.rental.service.CarService;
import com.rental_car_project_backend.car.rental.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final ImageUploadService imageUploadService;

    @Override
    public CreateCarResponse create(CreateCarRequest request) throws IOException {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();

        if (!authenticated) {
            throw new SecurityException("You must logged in first!");
        }

        String imageUrl = imageUploadService.uploadImage(request.getImageFile());

        Cars cars = new Cars();
        cars.setBaggages(request.getBaggages());
        cars.setName(request.getName());
        cars.setImage(imageUrl);
        cars.setYear(request.getYear());
        cars.setSeats(request.getSeats());
        cars.setCreatedAt(LocalDateTime.now());
        Cars save = carRepository.save(cars);
        return CreateCarResponse.builder()
                .name(save.getName())
                .id(save.getId())
                .createdAt(save.getCreatedAt())
                .updatedAt(save.getUpdatedAt())
                .image(save.getImage())
                .seats(save.getSeats())
                .year(save.getYear())
                .baggages(save.getBaggages())
                .build();
    }

    @Override
    public GetCarResponse getCar(Integer id) {
        Cars car = carRepository.findById(id).orElseThrow(()
                -> new CarNotFoundException("Car with id " + id + " not found"));
        return GetCarResponse.builder()
                .id(car.getId())
                .year(car.getYear())
                .seats(car.getSeats())
                .name(car.getName())
                .baggages(car.getBaggages())
                .createdAt(car.getCreatedAt())
                .image(car.getImage())
                .updatedAt(car.getUpdatedAt())
                .build();
    }

    @Override
    public List<GetCarResponse> getCars() {
        List<Cars> all = carRepository.findAll();
        return all.stream()
                .map(car -> GetCarResponse.builder()
                        .id(car.getId())
                        .name(car.getName())
                        .year(car.getYear())
                        .seats(car.getSeats())
                        .image(car.getImage())
                        .baggages(car.getBaggages())
                        .createdAt(car.getCreatedAt())
                        .updatedAt(car.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public DeleteCarResponse deleteCarById(Integer id) {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        Cars cars = carRepository.findById(id).orElseThrow(()
                -> new CarNotFoundException("Car with id " + id + " not found"));
        carRepository.deleteById(cars.getId());
        return DeleteCarResponse.builder()
                .id(cars.getId())
                .message("Success delete car with id " + id)
                .build();
    }

    @Override
    public UpdateCarResponse updateCar(Integer id, UpdateCarRequest request) {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        Cars cars = carRepository.findById(id).orElseThrow(()
                -> new CarNotFoundException("Car with id " + id + " not found"));
        cars.setId(id);
        if(Objects.nonNull(request.getName())){
            cars.setName(request.getName());
        }
        if(Objects.nonNull(request.getYear())){
            cars.setYear(request.getYear());
        }
        if(Objects.nonNull(request.getSeats())){
            cars.setSeats(request.getSeats());
        }
        if(Objects.nonNull(request.getBaggages())){
            cars.setBaggages(request.getBaggages());
        }
        if(Objects.nonNull(request.getImage())){
            cars.setImage(request.getImage());
        }
        cars.setCreatedAt(cars.getCreatedAt());
        cars.setUpdatedAt(LocalDateTime.now());
        Cars car = carRepository.save(cars);
        return UpdateCarResponse.builder()
                .id(car.getId())
                .year(car.getYear())
                .seats(car.getSeats())
                .name(car.getName())
                .baggages(car.getBaggages())
                .createdAt(car.getCreatedAt())
                .image(car.getImage())
                .updatedAt(car.getUpdatedAt())
                .build();
    }
}

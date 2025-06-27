package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.CreateCarRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateCarResponse;
import com.rental_car_project_backend.car.rental.entity.Cars;
import com.rental_car_project_backend.car.rental.repository.CarRepository;
import com.rental_car_project_backend.car.rental.service.impl.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    @Override
    public CreateCarResponse create(CreateCarRequest request) {
        System.out.println("✅ Authenticated: " + SecurityContextHolder.getContext().getAuthentication());
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();

        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        Cars cars = new Cars();
        cars.setBaggages(request.getBaggages());
        cars.setName(request.getName());
        cars.setImage(request.getImage());
        cars.setYear(request.getYear());
        cars.setSeats(request.getSeats());
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
}

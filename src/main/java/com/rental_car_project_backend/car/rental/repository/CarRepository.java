package com.rental_car_project_backend.car.rental.repository;

import com.rental_car_project_backend.car.rental.entity.Cars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Cars, Integer> {
}

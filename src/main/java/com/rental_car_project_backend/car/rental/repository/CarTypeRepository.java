package com.rental_car_project_backend.car.rental.repository;

import com.rental_car_project_backend.car.rental.entity.CarTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarTypeRepository extends JpaRepository<CarTypes, Integer> {
}

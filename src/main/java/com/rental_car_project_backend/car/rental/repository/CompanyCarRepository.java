package com.rental_car_project_backend.car.rental.repository;

import com.rental_car_project_backend.car.rental.entity.CompanyCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyCarRepository extends JpaRepository<CompanyCar, Integer> {
}

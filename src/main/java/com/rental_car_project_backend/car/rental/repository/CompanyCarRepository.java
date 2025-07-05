package com.rental_car_project_backend.car.rental.repository;

import com.rental_car_project_backend.car.rental.entity.CompanyCar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyCarRepository extends JpaRepository<CompanyCar, Integer> {
    Optional<CompanyCar> findByIdCompany(Integer id);
}

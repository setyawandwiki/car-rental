package com.rental_car_project_backend.car.rental.repository;

import com.rental_car_project_backend.car.rental.entity.CompanyCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CompanyCarRepository extends JpaRepository<CompanyCar, Integer>, JpaSpecificationExecutor<CompanyCar> {
    Optional<CompanyCar> findByIdCompany(Integer id);
}

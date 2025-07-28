package com.rental_car_project_backend.car.rental.repository;

import com.rental_car_project_backend.car.rental.entity.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CityRepository extends JpaRepository<Cities, Integer>, JpaSpecificationExecutor<Cities> {
}

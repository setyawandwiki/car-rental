package com.rental_car_project_backend.car.rental.repository;

import com.rental_car_project_backend.car.rental.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
}

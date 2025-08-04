package com.rental_car_project_backend.car.rental.repository;

import com.rental_car_project_backend.car.rental.entity.Payments;
import com.rental_car_project_backend.car.rental.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payments, Integer> {
    @Query(value = """
            SELECT p.* FROM payments p WHERE p.external_id = :external_id
            """, nativeQuery = true)
    Optional<Payments> findByExternalId(@Param("external_id") String externalId);
    List<Payments> findByOrderStatus(OrderStatus status);
}

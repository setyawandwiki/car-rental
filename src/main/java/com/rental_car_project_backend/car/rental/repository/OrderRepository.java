package com.rental_car_project_backend.car.rental.repository;

import com.rental_car_project_backend.car.rental.entity.Orders;
import com.rental_car_project_backend.car.rental.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
    @Query(value = """
            SELECT o.* FROM orders o WHERE o.id_user = :id 
            """, nativeQuery = true)
    List<Orders> userOrders(@Param("id") Integer id);
}

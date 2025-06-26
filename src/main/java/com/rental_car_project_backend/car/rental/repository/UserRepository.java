package com.rental_car_project_backend.car.rental.repository;

import com.rental_car_project_backend.car.rental.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query(value = """
            SELECT u.* FROM users u WHERE u.email = :email 
            """, nativeQuery = true)
    Optional<Users> findByEmail(@Param("email") String email);
}

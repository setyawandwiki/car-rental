package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.CreateOrderRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateOrderResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetCompanyResponse;
import com.rental_car_project_backend.car.rental.entity.Orders;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.enums.OrderStatus;
import com.rental_car_project_backend.car.rental.exceptions.UserNotFoundException;
import com.rental_car_project_backend.car.rental.repository.OrderRepository;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import com.rental_car_project_backend.car.rental.service.CarService;
import com.rental_car_project_backend.car.rental.service.CompanyCarService;
import com.rental_car_project_backend.car.rental.service.CompanyService;
import com.rental_car_project_backend.car.rental.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CompanyService companyService;
    private final CarService carService;
    private final UserRepository userRepository;
    private final CompanyCarService companyCarService;
    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("there is no user with email " + email));
        GetCompanyCarResponse companyCar = companyCarService.findCompanyCar(request.getIdCompanyCars());
        GetCompanyResponse company = companyService.findCompany(companyCar.getIdCompany());
        GetCarResponse car = carService.getCar(companyCar.getIdCar());
        Orders orders = new Orders();
        orders.setIdCompanyCars(request.getIdCompanyCars());
        orders.setStatus(OrderStatus.PENDING);
        orders.setIdUser(user.getId());
        orders.setCreatedAt(LocalDateTime.now());
        orders.setDropOffLoc(request.getDropOffLoc());
        orders.setPickupLoc(request.getPickupLoc());
        orders.setDropOffDate(request.getDropOffDate());
        orders.setPickupDate(request.getPickupDate());
        orders.setCreatedAt(LocalDateTime.now());
        Orders save = orderRepository.save(orders);
        return CreateOrderResponse.builder()
                .id(save.getId())
                .createdAt(save.getCreatedAt())
                .updatedate(save.getUpdatedAt())
                .carResponse(car)
                .companyResponse(company)
                .dropoff_loc(save.getDropOffLoc())
                .pickupDate(save.getPickupDate())
                .dropoffDate(save.getDropOffDate())
                .dropoffDate(save.getPickupDate())
                .priceTotal(save.getPriceTotal())
                .idUser(save.getIdUser())
                .status(save.getStatus())
                .idCompanyCars(save.getIdCompanyCars())
                .build();
    }

}

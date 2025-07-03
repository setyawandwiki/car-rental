package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.CreateOrderRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateOrderResponse;
import com.rental_car_project_backend.car.rental.entity.Orders;
import com.rental_car_project_backend.car.rental.repository.OrderRepository;
import com.rental_car_project_backend.car.rental.service.CarService;
import com.rental_car_project_backend.car.rental.service.CompanyService;
import com.rental_car_project_backend.car.rental.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CompanyService companyService;
    private final CarService carService;
    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        Orders orders = new Orders();
        orders.setIdCompanyCars(request.getIdCompanyCars());
        orders.setIdStatus();
        return null;
    }
}

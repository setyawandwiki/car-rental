package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.CreateOrderRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateOrderResponse;
import com.rental_car_project_backend.car.rental.dto.response.DeleteOrderResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetOrderResponse;

import java.util.List;

public interface OrderService {
    CreateOrderResponse createOrder(CreateOrderRequest request);
    List<GetOrderResponse> getOrderResponse();
    DeleteOrderResponse deleteOrder(Integer id);
}

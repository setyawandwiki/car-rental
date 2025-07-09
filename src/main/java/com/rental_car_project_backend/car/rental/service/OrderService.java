package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.order.CreateOrderRequest;
import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.order.CreateOrderResponse;
import com.rental_car_project_backend.car.rental.dto.response.order.DeleteOrderResponse;
import com.rental_car_project_backend.car.rental.dto.response.order.GetOrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    CreateOrderResponse createOrder(CreateOrderRequest request);
    DeleteOrderResponse deleteOrder(Integer id);
    Page<GetOrderResponse> getUserOrders(PageRequestDTO pageRequestDTO);
}

package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.CreateOrderRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreateOrderResponse;
import com.rental_car_project_backend.car.rental.dto.response.DeleteOrderResponse;
import com.rental_car_project_backend.car.rental.dto.response.GetOrderResponse;
import com.rental_car_project_backend.car.rental.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request){
        CreateOrderResponse order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    @GetMapping
    public ResponseEntity<List<GetOrderResponse>> getOrderUser(){
        List<GetOrderResponse> orderResponse = orderService.getOrderResponse();
        return ResponseEntity.ok().body(orderResponse);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DeleteOrderResponse> deleteOrder(@PathVariable("id") Integer id){
        DeleteOrderResponse deleteOrderResponse = orderService.deleteOrder(id);
        return ResponseEntity.ok().body(deleteOrderResponse);
    }
}

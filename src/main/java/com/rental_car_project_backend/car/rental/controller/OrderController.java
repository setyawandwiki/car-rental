package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.order.CreateOrderRequest;
import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.order.CreateOrderResponse;
import com.rental_car_project_backend.car.rental.dto.response.order.DeleteOrderResponse;
import com.rental_car_project_backend.car.rental.dto.response.order.GetOrderResponse;
import com.rental_car_project_backend.car.rental.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request){
        CreateOrderResponse order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    @GetMapping
    public ResponseEntity<Page<GetOrderResponse>> getOrderUser(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) String pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) String pageSize,
            @RequestParam(value = "sort", defaultValue = "ASC", required = false) Sort.Direction sort)
    {
        PageRequestDTO page = PageRequestDTO.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sort(sort)
                .build();
        Page<GetOrderResponse> orderResponse = orderService.getUserOrders(page);
        return ResponseEntity.ok().body(orderResponse);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DeleteOrderResponse> deleteOrder(@PathVariable("id") Integer id){
        DeleteOrderResponse deleteOrderResponse = orderService.deleteOrder(id);
        return ResponseEntity.ok().body(deleteOrderResponse);
    }
}

package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.order.CreateOrderRequest;
import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.car.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company.GetCompanyResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.GetCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.order.CreateOrderResponse;
import com.rental_car_project_backend.car.rental.dto.response.order.DeleteOrderResponse;
import com.rental_car_project_backend.car.rental.dto.response.order.GetOrderResponse;
import com.rental_car_project_backend.car.rental.entity.Orders;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.enums.OrderStatus;
import com.rental_car_project_backend.car.rental.exceptions.OrdersNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.UserNotFoundException;
import com.rental_car_project_backend.car.rental.repository.OrderRepository;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import com.rental_car_project_backend.car.rental.service.CarService;
import com.rental_car_project_backend.car.rental.service.CompanyCarService;
import com.rental_car_project_backend.car.rental.service.CompanyService;
import com.rental_car_project_backend.car.rental.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CompanyService companyService;
    private final CarService carService;
    private final UserRepository userRepository;
    private final CompanyCarService companyCarService;
    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(request.getPickupDate().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Pickup date cannot lower than today");
        }
        if(request.getDropOffDate().isBefore(request.getPickupDate())){
            throw new IllegalArgumentException("Pickup date cannot lower than your pick up date");
        }
        Users user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("there is no user with email " + email));
        GetCompanyCarResponse companyCar = companyCarService.findCompanyCar(request.getIdCompanyCars());
        GetCompanyResponse company = companyService.findCompany(companyCar.getIdCompany());
        GetCarResponse carResponse = carService.getCar(companyCar.getIdCar());

        LocalDateTime pickUpDate = request.getPickupDate();
        LocalDateTime dropOffDate = request.getDropOffDate();

        long hours = Duration.between(pickUpDate, dropOffDate).toHours();
        Double totalDays = Math.ceil(hours / 24.0);
        Double totalPrice = companyCar.getPrice() * totalDays;

        Orders orders = new Orders();
        orders.setIdCompanyCars(request.getIdCompanyCars());
        orders.setStatus(OrderStatus.PENDING);
        orders.setIdUser(user.getId());
        orders.setCreatedAt(LocalDateTime.now());
        orders.setPriceTotal(totalPrice);
        orders.setDropOffLoc(request.getDropOffLoc());
        orders.setPickupLoc(request.getPickupLoc());
        orders.setDropOffDate(request.getDropOffDate());
        orders.setPickupDate(request.getPickupDate());
        orders.setCreatedAt(LocalDateTime.now());
        Orders save = orderRepository.save(orders);
        return CreateOrderResponse.builder()
                .id(save.getId())
                .createdAt(save.getCreatedAt())
                .carResponse(carResponse)
                .companyResponse(company)
                .dropOffLoc(save.getDropOffLoc())
                .pickUpDate(save.getPickupDate())
                .pickUpLoc(save.getPickupLoc())
                .dropOffDate(save.getDropOffDate())
                .priceTotal(save.getPriceTotal())
                .idUser(save.getIdUser())
                .status(save.getStatus())
                .idCompanyCars(save.getIdCompanyCars())
                .build();
    }

    @Override
    @Transactional
    public DeleteOrderResponse deleteOrder(Integer id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("there is no user with email " + email));
        List<Orders> orders = orderRepository.userOrders(user.getId())
                .stream()
                .filter(val -> val.getStatus() == OrderStatus.PENDING).toList();
        if(orders.isEmpty()){
            throw new OrdersNotFoundException("Order not found");
        }
        Orders orders1 = orderRepository.findById(id).orElseThrow(() ->
                new OrdersNotFoundException("Order not found with id " + id));
        orders1.setStatus(OrderStatus.CANCELLED);
        return DeleteOrderResponse.builder()
                .id(orders1.getId())
                .message("Delete Order Success")
                .build();
    }

    @Override
    @Transactional
    public Page<GetOrderResponse> getUserOrders(PageRequestDTO pageRequestDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("there is no user with email " + email));
        Sort sort = Sort.by(pageRequestDTO.getSort(), "created_at");
        Pageable pageRequest = PageRequest
                .of(Integer.parseInt(pageRequestDTO.getPageNo()), Integer.parseInt(pageRequestDTO.getPageSize()), sort);
        Page<Orders> orders = orderRepository.userOrders(user.getId(), pageRequest);
        return orders.map(val -> {
            GetCompanyCarResponse companyCar = companyCarService.findCompanyCar(val.getIdCompanyCars());
            GetCarResponse car = carService.getCar(companyCar.getIdCar());
            GetCompanyResponse company = companyService.findCompany(companyCar.getIdCompany());
            GetOrderResponse orders1 = new GetOrderResponse();
            orders1.setIdCompanyCars(val.getIdCompanyCars());
            orders1.setStatus(val.getStatus());
            orders1.setIdUser(val.getIdUser());
            orders1.setCreatedAt(val.getCreatedAt());
            orders1.setDropoff_loc(val.getDropOffLoc());
            orders1.setPickupLoc(val.getPickupLoc());
            orders1.setDropoffDate(val.getDropOffDate());
            orders1.setPickupDate(val.getPickupDate());
            orders1.setUpdateAt(val.getUpdatedAt());
            orders1.setId(val.getId());
            orders1.setPriceTotal(val.getPriceTotal());
            orders1.setCarResponse(car);
            orders1.setCompanyResponse(company);

            return orders1;
        });
    }
}

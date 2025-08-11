package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.order.CreateOrderRequest;
import com.rental_car_project_backend.car.rental.enums.OrderStatus;
import com.rental_car_project_backend.car.rental.exceptions.UserNotFoundException;
import com.rental_car_project_backend.car.rental.repository.OrderRepository;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import com.rental_car_project_backend.car.rental.service.CarService;
import com.rental_car_project_backend.car.rental.service.CompanyCarService;
import com.rental_car_project_backend.car.rental.service.CompanyService;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CompanyService companyService;
    @Mock
    private CarService carService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CompanyCarService companyCarService;
    @InjectMocks
    private OrderServiceImpl orderService;
    private CreateOrderRequest createOrderRequest;
    @BeforeEach
    void setUp(){
        createOrderRequest = CreateOrderRequest.builder()
                .createdAt(LocalDateTime.now())
                .idUser(1)
                .dropOffLoc("cibinong")
                .dropOffDate(LocalDateTime.now().plusHours(24L))
                .idCompanyCars(1)
                .priceTotal(20000.)
                .pickupDate(LocalDateTime.now().plusDays(1))
                .pickupLoc("cibinong")
                .build();
    }
    @Test
    void orderServiceImpl_CreateOrderMethodShouldReturnPickUpDateCannotLowerThanInput() {
        // given
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(fakeAuthentication);
        Mockito.when(securityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(securityContext.getAuthentication().getName()).thenReturn("test@gmail.com");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                orderService.createOrder(createOrderRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("there is no user with email test@gmail.com");
    }

    @Test
    void deleteOrder() {
    }

    @Test
    void getUserOrders() {
    }
}
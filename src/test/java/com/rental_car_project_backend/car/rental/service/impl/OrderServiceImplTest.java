package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.order.CreateOrderRequest;
import com.rental_car_project_backend.car.rental.dto.response.car.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.company.GetCompanyResponse;
import com.rental_car_project_backend.car.rental.dto.response.company_car.GetCompanyCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.order.CreateOrderResponse;
import com.rental_car_project_backend.car.rental.dto.response.order.DeleteOrderResponse;
import com.rental_car_project_backend.car.rental.entity.CompanyCar;
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
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Collections;
import java.util.List;
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
    @Captor
    ArgumentCaptor<Orders> ordersArgumentCaptor;
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
    void orderServiceImpl_CreateOrderMethodShouldReturnThereIsNoUserWithEmail() {
        // given
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
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
    void orderServiceImpl_CreateOrderMethodShouldSucceed() {
        Users users = Users.builder()
                .id(1)
                .accountNumber("123456789")
                .bankCode("BCA")
                .email("test@gmail.com")
                .fullName("dwiki setyawan")
                .idRole(1)
                .createdAt(LocalDateTime.now())
                .build();
        CompanyCar companyCar = CompanyCar.builder()
                .id(1)
                .idCompany(1)
                .idCar(1)
                .price(200.)
                .idCarType(1)
                .createdAt(LocalDateTime.now())
                .build();
        GetCompanyCarResponse getCompanyCarResponse = GetCompanyCarResponse.builder()
                .idCompany(1)
                .idCar(1)
                .price(2000.)
                .build();
        GetCompanyResponse getCompanyResponse = new GetCompanyResponse();
        GetCarResponse getCarResponse = GetCarResponse.builder()
                .id(1)
                .build();
        Orders orders = new Orders();
        orders.setIdCompanyCars(createOrderRequest.getIdCompanyCars());
        orders.setStatus(OrderStatus.PENDING);
        orders.setIdUser(users.getId());
        orders.setCreatedAt(LocalDateTime.now());
        orders.setPriceTotal(123512313.);
        orders.setDropOffLoc(createOrderRequest.getDropOffLoc());
        orders.setPickupLoc(createOrderRequest.getPickupLoc());
        orders.setDropOffDate(createOrderRequest.getDropOffDate());
        orders.setPickupDate(createOrderRequest.getPickupDate());
        orders.setCreatedAt(LocalDateTime.now());
        // given
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(securityContext.getAuthentication().getName()).thenReturn("test@gmail.com");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(users));
        Mockito.when(companyCarService.findCompanyCar(Mockito.anyInt())).thenReturn(getCompanyCarResponse);
        Mockito.when(companyService.findCompany(Mockito.anyInt())).thenReturn(getCompanyResponse);
        Mockito.when(carService.getCar(Mockito.anyInt())).thenReturn(getCarResponse);
        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(orders);
        // when
        orderService.createOrder(createOrderRequest);
        // then
        Mockito.verify(orderRepository).save(ordersArgumentCaptor.capture());
        Orders value = ordersArgumentCaptor.getValue();
        assertThat(value.getDropOffLoc()).isEqualTo(createOrderRequest.getDropOffLoc());
        assertThat(value.getIdCompanyCars()).isEqualTo(orders.getIdCompanyCars());
    }

    @Test
    void orderServiceImpl_DeleteOrderMethodShouldReturnThereIsNoUserWithThatEmail() {
        // given
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(securityContext.getAuthentication().getName()).thenReturn("test@gmail.com");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                orderService.deleteOrder(1))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("There is no user with email test@gmail.com");
    }

    @Test
    void orderServiceImpl_DeleteOrderMethodShouldReturnOrderNotFound() {
        // given
        Users users = Users.builder()
                .id(1)
                .email("test@gmail.com")
                .build();
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(securityContext.getAuthentication().getName()).thenReturn("test@gmail.com");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(users));
        Mockito.when(orderRepository.userOrders(Mockito.anyInt())).thenReturn(Collections.emptyList());
        // when
        // then
        assertThatThrownBy(()->
                orderService.deleteOrder(1))
                .isInstanceOf(OrdersNotFoundException.class)
                .hasMessageContaining("Order not found");

    }

    @Test
    void orderServiceImpl_DeleteOrderMethodShouldReturnOrderNotFoundWithId() {
        // given
        Users users = Users.builder()
                .id(1)
                .email("test@gmail.com")
                .build();
        Orders orders = Orders.builder()
                .id(1)
                .createdAt(LocalDateTime.now())
                .dropOffDate(LocalDateTime.now().plusDays(1))
                .dropOffLoc("cibinong")
                .pickupDate(LocalDateTime.now().plusDays(1))
                .pickupLoc("cibinong")
                .status(OrderStatus.PENDING)
                .idUser(1)
                .priceTotal(2000.)
                .build();
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(securityContext.getAuthentication().getName()).thenReturn("test@gmail.com");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(users));
        Mockito.when(orderRepository.userOrders(Mockito.anyInt())).thenReturn(List.of(orders));
        Mockito.when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                orderService.deleteOrder(1))
                .isInstanceOf(OrdersNotFoundException.class)
                .hasMessageContaining("Order not found with id " + 1);

    }

    @Test
    void orderServiceImpl_DeleteOrderMethodShouldSucceed() {
        // given
        Users users = Users.builder()
                .id(1)
                .email("test@gmail.com")
                .build();
        Orders orders = Orders.builder()
                .id(1)
                .createdAt(LocalDateTime.now())
                .dropOffDate(LocalDateTime.now().plusDays(1))
                .dropOffLoc("cibinong")
                .pickupDate(LocalDateTime.now().plusDays(1))
                .pickupLoc("cibinong")
                .status(OrderStatus.PENDING)
                .idUser(1)
                .priceTotal(2000.)
                .build();
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(securityContext.getAuthentication().getName()).thenReturn("test@gmail.com");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(users));
        Mockito.when(orderRepository.userOrders(Mockito.anyInt())).thenReturn(List.of(orders));
        Mockito.when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(orders));
        // when
        DeleteOrderResponse deleteOrderResponse = orderService.deleteOrder(1);
        // then
        assertThat("Delete Order Success").isEqualTo(deleteOrderResponse.getMessage());
        assertThat(orders.getStatus()).isEqualTo(OrderStatus.CANCELLED);

    }

    @Test
    void orderServiceImpl_GetUserOrdersShouldReturnThereIsNoUserWithThatEmail() {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(securityContext.getAuthentication().getName()).thenReturn("test@gmail.com");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                orderService.deleteOrder(1))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("There is no user with email test@gmail.com");
    }
}
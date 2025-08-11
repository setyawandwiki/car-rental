package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.company_car.CreateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.enums.CompanyCarStatus;
import com.rental_car_project_backend.car.rental.repository.*;
import com.rental_car_project_backend.car.rental.service.CarService;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CompanyCarServiceImplTest {
    @Mock
    private CompanyCarRepository companyCarRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private CarTypeRepository carTypeRepository;
    @Mock
    private CarService carService;
    @Mock
    private CompanyService companyService;
    @InjectMocks
    private CompanyCarServiceImpl companyCarService;
    private CreateCompanyCarRequest companyCarRequest;
    @BeforeEach
    void setUp(){
        companyCarRequest = CreateCompanyCarRequest.builder()
                .idCar(1)
                .idCompany(1)
                .price(1000.)
                .createdAt(LocalDateTime.now())
                .status(CompanyCarStatus.ACTIVE)
                .build();
    }
    @Test
    void companyCarServiceImpl_CreateMethodShouldReturnYouAreNotLogin() {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(false);
        // when
        // then
        assertThatThrownBy(()->
                companyCarService.createCompanyCar(companyCarRequest))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("You must logged in first!");
    }
    @Test
    void companyCarServiceImpl_CreateMethodShouldReturnYouAreNotLogin() {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(false);
        // when
        // then
        assertThatThrownBy(()->
                companyCarService.createCompanyCar(companyCarRequest))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("You must logged in first!");
    }

    @Test
    void getCompanyCars() {
    }

    @Test
    void deleteCompanyCar() {
    }

    @Test
    void updateCompanyCar() {
    }

    @Test
    void findCompanyCar() {
    }
}
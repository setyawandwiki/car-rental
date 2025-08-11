package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.company_car.CreateCompanyCarRequest;
import com.rental_car_project_backend.car.rental.entity.*;
import com.rental_car_project_backend.car.rental.enums.CompanyCarStatus;
import com.rental_car_project_backend.car.rental.exceptions.CarNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.CompanyNotFoundException;
import com.rental_car_project_backend.car.rental.repository.*;
import com.rental_car_project_backend.car.rental.service.CarService;
import com.rental_car_project_backend.car.rental.service.CompanyService;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    @Captor
    ArgumentCaptor<CompanyCar> companyCarArgumentCaptor;
    @InjectMocks
    private CompanyCarServiceImpl companyCarService;
    private CreateCompanyCarRequest companyCarRequest;
    @BeforeEach
    void setUp(){
        companyCarRequest = CreateCompanyCarRequest.builder()
                .idCar(1)
                .idCompany(1)
                .price(1000.)
                .idCar(1)
                .idCarType(1)
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
    void companyCarServiceImpl_CreateMethodShouldReturnCompanyNotFound() {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(companyRepository.findById(companyCarRequest.getIdCompany())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                companyCarService.createCompanyCar(companyCarRequest))
                .isInstanceOf(CompanyNotFoundException.class)
                .hasMessageContaining("Company not found with id " + companyCarRequest.getIdCompany());
    }

    @Test
    void companyCarServiceImpl_CreateMethodShouldReturnCarNotFound() {
        // given
        Companies companies = Companies.builder()
                .idCity(1)
                .name("BMW")
                .createdAt(LocalDateTime.now())
                .rate(3.5)
                .image("test.jpg")
                .idUser(1)
                .build();
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(companyRepository.findById(companyCarRequest.getIdCompany()))
                        .thenReturn(Optional.of(companies));
        Mockito.when(carRepository.findById(companyCarRequest.getIdCar())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                companyCarService.createCompanyCar(companyCarRequest))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining("Car not found with id " + companyCarRequest.getIdCar());
    }

    @Test
    void companyCarServiceImpl_CreateMethodShouldSucceed() {
        // given
        Cars cars = Cars.builder()
                .image("test.jpg")
                .id(1)
                .year(2018)
                .name("testing")
                .build();
        Countries countries = Countries.builder()
                .id(1)
                .name("indonesiia")
                .build();
        Cities cities = Cities.builder()
                .name("cibinong")
                .id(1)
                .idCountry(1)
                .idCountry(countries.getId())
                .build();
        Companies companies = Companies.builder()
                .idCity(1)
                .cities(cities)
                .name("BMW")
                .createdAt(LocalDateTime.now())
                .rate(3.5)
                .image("test.jpg")
                .idUser(1)
                .build();
        CompanyCar companyCar = CompanyCar.builder()
                .company(companies)
                .idCompany(1)
                .idCarType(1)
                .idCar(1)
                .price(2000.)
                .createdAt(LocalDateTime.now())
                .status(CompanyCarStatus.ACTIVE)
                .build();
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(companyRepository.findById(companyCarRequest.getIdCompany()))
                .thenReturn(Optional.of(companies));
        Mockito.when(carRepository.findById(companyCarRequest.getIdCar())).thenReturn(Optional.of(cars));
        Mockito.when(companyCarRepository.save(Mockito.any(CompanyCar.class))).thenReturn(companyCar);
        // when
        companyCarService.createCompanyCar(companyCarRequest);
        // then
        Mockito.verify(companyCarRepository).save(companyCarArgumentCaptor.capture());
        CompanyCar value = companyCarArgumentCaptor.getValue();
        assertThat(companyCar.getId()).isEqualTo(value.getId());
        assertThat(companyCar.getIdCarType()).isEqualTo(value.getIdCarType());
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
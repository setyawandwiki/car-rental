package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.car.CreateCarRequest;
import com.rental_car_project_backend.car.rental.entity.Cars;
import com.rental_car_project_backend.car.rental.repository.CarRepository;
import com.rental_car_project_backend.car.rental.service.ImageUploadService;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;
    @Mock
    private ImageUploadService imageUploadService;
    @InjectMocks
    private CarServiceImpl carService;
    @Captor
    ArgumentCaptor<Cars> carsArgumentCaptor;
    CreateCarRequest request;

    @BeforeEach
    public void setUp(){
        request = CreateCarRequest.builder()
                .name("test")
                .baggages(2)
                .seats(2)
                .year(2024)
                .imageFile(new MockMultipartFile("test", new byte[]{}))
                .build();
    }

    @Test
    void carService_shouldReturnSecurityException() {
        // given
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(context);
        Mockito.when(context.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(false);
        // when
        // then
        assertThatThrownBy(()->
                carService.create(request))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("You must logged in first!");
        Mockito.verify(carRepository, Mockito.times(0)).save(Mockito.any(Cars.class));
    }

    @Test
    void carServiceImpl_shouldUploadImage() throws IOException {
        // given
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(imageUploadService.uploadImage(Mockito.eq(request.getImageFile()),
                Mockito.anyString(),
                Mockito.eq("car-images"))).thenReturn("Mockito.anyString()");
        Cars dummySavedCar = new Cars();
        dummySavedCar.setId(1);
        dummySavedCar.setName("test");
        dummySavedCar.setImage("mock-url");
        dummySavedCar.setSeats(request.getSeats());
        dummySavedCar.setYear(request.getYear());
        dummySavedCar.setBaggages(request.getBaggages());
        dummySavedCar.setCreatedAt(LocalDateTime.now());
        dummySavedCar.setUpdatedAt(LocalDateTime.now());

        Mockito.when(carRepository.save(Mockito.any(Cars.class))).thenReturn(dummySavedCar);
        // when
        carService.create(request);
        // then
        Mockito.verify(carRepository).save(carsArgumentCaptor.capture());
        Mockito.verify(imageUploadService, Mockito.times(1))
                .uploadImage(Mockito.eq(request.getImageFile()),
                        Mockito.any(String.class),
                        Mockito.eq("car-images"));
    }

    @Test
    void getCars() {
    }

    @Test
    void deleteCarById() {
    }

    @Test
    void updateCar() {
    }
}
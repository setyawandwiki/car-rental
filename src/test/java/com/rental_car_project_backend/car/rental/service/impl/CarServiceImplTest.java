package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.car.CreateCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.car.GetCarResponse;
import com.rental_car_project_backend.car.rental.entity.Cars;
import com.rental_car_project_backend.car.rental.exceptions.CarNotFoundException;
import com.rental_car_project_backend.car.rental.repository.CarRepository;
import com.rental_car_project_backend.car.rental.service.ImageUploadService;
import jakarta.persistence.criteria.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    List<Predicate> predicates = new ArrayList<>();
    Cars mockCar;

    @BeforeEach
    public void setUp(){
        mockCar = new Cars();
        mockCar.setId(1);
        mockCar.setName("Toyota");
        mockCar.setSeats(4);
        mockCar.setBaggages(2);
        mockCar.setYear(2023);
        mockCar.setImage("image-url");
        mockCar.setCreatedAt(LocalDateTime.now());
        mockCar.setUpdatedAt(LocalDateTime.now());
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
    void carServiceImpl_shouldSuccess() throws IOException {
        // given
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(imageUploadService.uploadImage(Mockito.eq(request.getImageFile()),
                Mockito.anyString(),
                Mockito.eq("car-images"))).thenReturn("image-url");
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
        Mockito.verify(imageUploadService, Mockito.times(1))
                .uploadImage(Mockito.eq(request.getImageFile()), Mockito.anyString(), Mockito.eq("car-images"));
    }

    @Test
    void carServiceImplGetCars_shouldReturnSecurityException() {
        // given
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        Mockito.when(auth.isAuthenticated()).thenReturn(false);
        // when
        // then
        assertThatThrownBy(()->
                carService.getCar(1))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("You must logged in first!");
    }

    @Test
    void carServiceImplGetCars_shouldReturnCarNotFound() {
        // given
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        Mockito.when(auth.isAuthenticated()).thenReturn(true);
        Mockito.when(carRepository.findById(1)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                carService.getCar(1))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining("Car with id " + 1 + " not found");
    }

    @Test
    void carServiceImplGetCars_shouldReturnCar() {
        // given
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        Mockito.when(auth.isAuthenticated()).thenReturn(true);
        Mockito.when(carRepository.findById(1)).thenReturn(Optional.of(new Cars()));
        // when
        carService.getCar(1);
        // then
        Mockito.verify(carRepository).findById(1);
    }

    @Test
    void carServiceImpl_shouldAddPredicateArray() {
        // Given
        SearchRequestDTO searchRequest = new SearchRequestDTO();
        searchRequest.setValue("Toy");

        PageRequestDTO pageRequest = new PageRequestDTO();
        pageRequest.setPageNo("0");
        pageRequest.setPageSize("10");
        pageRequest.setSort(Sort.Direction.ASC);

        Page<Cars> carPage = new PageImpl<>(List.of(mockCar));

        ArgumentCaptor<Specification<Cars>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        Mockito.when(carRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(carPage);

        // When
        Page<GetCarResponse> result = carService.getCars(searchRequest, pageRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        GetCarResponse carResponse = result.getContent().get(0);
        assertThat(carResponse.getName()).isEqualTo("Toyota");

        Mockito.verify(carRepository).findAll(specCaptor.capture(), pageableCaptor.capture());

        Pageable usedPageable = pageableCaptor.getValue();
        assertThat(usedPageable.getPageNumber()).isEqualTo(0);
        assertThat(usedPageable.getPageSize()).isEqualTo(10);
        assertThat(usedPageable.getSort().getOrderFor("id").getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void updateCar() {
    }
}
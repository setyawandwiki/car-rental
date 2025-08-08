package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.car.CreateCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.car.UpdateCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.car.GetCarResponse;
import com.rental_car_project_backend.car.rental.entity.Cars;
import com.rental_car_project_backend.car.rental.exceptions.CarNotFoundException;
import com.rental_car_project_backend.car.rental.repository.CarRepository;
import com.rental_car_project_backend.car.rental.service.ImageUploadService;
import jakarta.persistence.criteria.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    @Captor
    ArgumentCaptor<Specification<Cars>> specificationArgumentCaptor;
    @Captor
    ArgumentCaptor<Pageable> pageableCaptor;
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
        // given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .pageNo("0")
                .pageSize("10")
                .sort(Sort.Direction.ASC)
                .build();
        SearchRequestDTO searchRequestDTO = SearchRequestDTO.builder()
                .value("BMW")
                .build();
        Cars cars1 = Cars.builder()
                .image("test")
                .baggages(2)
                .year(202)
                .name("BMW")
                .seats(2)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
        Cars cars2 = Cars.builder()
                .image("test")
                .baggages(2)
                .year(202)
                .name("VW")
                .seats(2)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
        List<Cars> cars = List.of(cars1, cars2);
        Page<Cars> mockedPage = new PageImpl<>(cars);
        carRepository.saveAll(cars);
        Sort sort = Sort.by(pageRequestDTO.getSort(), "id");
        Pageable pageable = PageRequest.of(Integer.parseInt(pageRequestDTO
                        .getPageNo()), Integer.parseInt(pageRequestDTO.getPageSize()),
                sort);
        Mockito.when(carRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockedPage);
        // when
        carService.getCars(searchRequestDTO, pageRequestDTO);
        // then
        Mockito.verify(carRepository).findAll(specificationArgumentCaptor.capture(), Mockito.eq(pageable));
        Specification<Cars> value = specificationArgumentCaptor.getValue();
        assertThat(carRepository.findAll(value, pageable).getContent().size()).isEqualTo(2);
    }

    @Test
    void carServiceImpl_deleteMethodShouldReturnNotLogin() {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeSecurityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeSecurityContext);
        Mockito.when(fakeSecurityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(false);
        // when
        // then
        assertThatThrownBy(()->
                carService.deleteCarById(1))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("You must logged in first!");
    }

    @Test
    void carServiceImpl_deleteMethodShouldReturnCarIdNotFound(){
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeSecurityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeSecurityContext);
        Mockito.when(fakeSecurityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(carRepository.findById(1)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                carService.deleteCarById(1))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining("Car with id " + 1 + " not found");
    }

    @Test
    void carServiceImpl_deleteDeleteCar() throws IOException {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeSecurityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeSecurityContext);
        Mockito.when(fakeSecurityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Cars cars = Cars.builder()
                .id(1)
                .image("test.img")
                .build();
        Mockito.when(carRepository.findById(1)).thenReturn(Optional.of(cars));
        // when
        carService.deleteCarById(1);
        // then
        Mockito.verify(carRepository).deleteById(cars.getId());
        Mockito.verify(imageUploadService).deleteImage(Mockito.any(String.class), Mockito.eq("car-images"));
    }

    @Test
    void carServiceImpl_updateMethodShouldReturnNotLogin() {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeSecurityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeSecurityContext);
        Mockito.when(fakeSecurityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(false);
        UpdateCarRequest updateCarRequest = UpdateCarRequest.builder()
                .baggages(1)
                .imageFile(new MockMultipartFile("test", new byte[]{}))
                .name("BMW")
                .seats(2)
                .year(2025)
                .build();
        // when
        // then
        assertThatThrownBy(()->
                carService.updateCar(1, updateCarRequest))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("You must logged in first!");
    }

    @Test
    void carServiceImpl_updateMethodShouldReturnCarIdNotFound(){
        // given
        UpdateCarRequest updateCarRequest = UpdateCarRequest.builder()
                .baggages(1)
                .imageFile(new MockMultipartFile("test", new byte[]{}))
                .name("BMW")
                .seats(2)
                .year(2025)
                .build();
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeSecurityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeSecurityContext);
        Mockito.when(fakeSecurityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(carRepository.findById(1)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                carService.updateCar(1, updateCarRequest))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining("Car with id " + 1 + " not found");
    }
    @Test
    void carServiceImpl_updateMethodSucceed() throws IOException {
        // given
        UpdateCarRequest updateCarRequest = UpdateCarRequest.builder()
                .baggages(1)
                /**
                 * agar kondisi ini terpenuhi, kalau pakai imageFile(new MockMultipartFile("test", new byte[]{}))
                 * artinya new byte[] {} artinya isi filenya kosong → isEmpty() akan return true.
                 * if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
                 *             String publicId = "car-" + id;
                 *             String imageUrl = imageUploadService.uploadImage(request.getImageFile(), publicId, "car-images");
                 *             cars.setImage(imageUrl);
                 *         }
                 * */
                .imageFile(new MockMultipartFile("test",
                        "car.jpg",
                        "image/jpeg",
                        "fake-image-content".getBytes()))
                .name("BMW")
                .seats(2)
                .year(2025)
                .build();
        Cars cars = Cars.builder()
                .baggages(1)
                .image(updateCarRequest.getImageFile().toString())
                .name(updateCarRequest.getName())
                .seats(updateCarRequest.getSeats())
                .year(updateCarRequest.getYear())
                .build();
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeSecurityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeSecurityContext);
        Mockito.when(fakeSecurityContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(carRepository.findById(1)).thenReturn(Optional.of(cars));
        Mockito.when(carRepository.save(cars)).thenReturn(cars);
        Mockito.when(imageUploadService.uploadImage(Mockito.eq(updateCarRequest.getImageFile()),
                        Mockito.anyString(),
                        Mockito.eq(
                                "car-images"))).thenReturn("image-url");
        // when
        carService.updateCar(1, updateCarRequest);
        // then
        Mockito.verify(imageUploadService).uploadImage(Mockito.eq(updateCarRequest.getImageFile()),
                Mockito.anyString(),
                Mockito.eq(
                "car-images"));
        Mockito.verify(carRepository).save(carsArgumentCaptor.capture());
        Cars value = carsArgumentCaptor.getValue();
        assertThat("BMW").isEqualTo(value.getName());
        assertThat(2).isEqualTo(value.getSeats());
    }
}
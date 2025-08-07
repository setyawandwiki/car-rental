package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.car.CreateCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.car.UpdateCarRequest;
import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.car.CreateCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.car.DeleteCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.car.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.car.UpdateCarResponse;
import com.rental_car_project_backend.car.rental.entity.Cars;
import com.rental_car_project_backend.car.rental.exceptions.CarNotFoundException;
import com.rental_car_project_backend.car.rental.repository.CarRepository;
import com.rental_car_project_backend.car.rental.service.CarService;
import com.rental_car_project_backend.car.rental.service.ImageUploadService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final ImageUploadService imageUploadService;

    @Override
    @Transactional
    public CreateCarResponse create(CreateCarRequest request) throws IOException {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();

        if (!authenticated) {
            throw new SecurityException("You must logged in first!");
        }
        String uniqueImageId = "car-" + UUID.randomUUID();
        String imageUrl = imageUploadService.uploadImage(request.getImageFile(), uniqueImageId, "car-images");

        Cars cars = new Cars();
        cars.setBaggages(request.getBaggages());
        cars.setName(request.getName());
        cars.setImage(imageUrl);
        cars.setYear(request.getYear());
        cars.setSeats(request.getSeats());
        cars.setCreatedAt(LocalDateTime.now());
        Cars save = carRepository.save(cars);
        return CreateCarResponse.builder()
                .name(save.getName())
                .id(save.getId())
                .createdAt(save.getCreatedAt())
                .updatedAt(save.getUpdatedAt())
                .image(save.getImage())
                .seats(save.getSeats())
                .year(save.getYear())
                .baggages(save.getBaggages())
                .build();
    }

    @Override
    public GetCarResponse getCar(Integer id) {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();

        if (!authenticated) {
            throw new SecurityException("You must logged in first!");
        }
        Cars car = carRepository.findById(id).orElseThrow(()
                -> new CarNotFoundException("Car with id " + id + " not found"));
        return GetCarResponse.builder()
                .id(car.getId())
                .year(car.getYear())
                .seats(car.getSeats())
                .name(car.getName())
                .baggages(car.getBaggages())
                .createdAt(car.getCreatedAt())
                .image(car.getImage())
                .updatedAt(car.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetCarResponse> getCars(SearchRequestDTO requestDTO, PageRequestDTO pageRequestDTO) {
        List<Predicate> predicates = new ArrayList<>();
        Specification<Cars> carsSpecification = (root,
                                                 query,
                                                 criteriaBuilder) -> {
            if(Objects.nonNull(requestDTO.getValue())){
                Predicate equal = criteriaBuilder.like(root.get("name"),
                        "%" +requestDTO.getValue() + "%");
                predicates.add(equal);
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
        Sort sort = Sort.by(pageRequestDTO.getSort(), "id");
        Pageable pageable = PageRequest.of(Integer.parseInt(pageRequestDTO
                .getPageNo()), Integer.parseInt(pageRequestDTO.getPageSize()),
                sort);
        Page<Cars> all = carRepository.findAll(carsSpecification, pageable);
        return all.map(car -> GetCarResponse.builder()
                .id(car.getId())
                .name(car.getName())
                .seats(car.getSeats())
                .baggages(car.getBaggages())
                .year(car.getYear())
                .image(car.getImage())
                .createdAt(car.getCreatedAt())
                .updatedAt(car.getUpdatedAt())
                .build()
        );
    }

    @Override
    public DeleteCarResponse deleteCarById(Integer id) throws IOException {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        Cars cars = carRepository.findById(id).orElseThrow(()
                -> new CarNotFoundException("Car with id " + id + " not found"));
        String urlImage = cars.getImage();
        String fileName = urlImage.substring(urlImage.lastIndexOf("/") + 1);
        String publicId = fileName.substring(0, fileName.lastIndexOf("."));
        carRepository.deleteById(cars.getId());
        imageUploadService.deleteImage(publicId, "car-images");
        return DeleteCarResponse.builder()
                .id(cars.getId())
                .message("Success delete car with id " + id)
                .build();
    }

    @Override
    @Transactional
    public UpdateCarResponse updateCar(Integer id, UpdateCarRequest request) throws IOException {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        Cars cars = carRepository.findById(id).orElseThrow(()
                -> new CarNotFoundException("Car with id " + id + " not found"));
        cars.setId(id);
        if(Objects.nonNull(request.getName())){
            cars.setName(request.getName());
        }
        if(Objects.nonNull(request.getYear())){
            cars.setYear(request.getYear());
        }
        if(Objects.nonNull(request.getSeats())){
            cars.setSeats(request.getSeats());
        }
        if(Objects.nonNull(request.getBaggages())){
            cars.setBaggages(request.getBaggages());
        }
        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            String publicId = "car-" + id;
            String imageUrl = imageUploadService.uploadImage(request.getImageFile(), publicId, "car-images");
            cars.setImage(imageUrl);
        }
        cars.setCreatedAt(cars.getCreatedAt());
        cars.setUpdatedAt(LocalDateTime.now());
        Cars car = carRepository.save(cars);
        return UpdateCarResponse.builder()
                .id(cars.getId())
                .year(car.getYear())
                .seats(car.getSeats())
                .name(car.getName())
                .baggages(car.getBaggages())
                .createdAt(car.getCreatedAt())
                .image(car.getImage())
                .updatedAt(car.getUpdatedAt())
                .build();
    }
}

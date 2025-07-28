package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchRequestDTO;
import com.rental_car_project_backend.car.rental.dto.response.car.GetCarResponse;
import com.rental_car_project_backend.car.rental.dto.response.city.GetCitiesResponse;
import com.rental_car_project_backend.car.rental.entity.Cars;
import com.rental_car_project_backend.car.rental.entity.Cities;
import com.rental_car_project_backend.car.rental.repository.CityRepository;
import com.rental_car_project_backend.car.rental.service.CityService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    @Override
    public Page<GetCitiesResponse> getCities(SearchRequestDTO requestDTO, PageRequestDTO pageRequestDTO) {
        List<Predicate> predicates = new ArrayList<>();
        Specification<Cities> citiesSpecification = (root,
                                                   query,
                                                   criteriaBuilder) -> {
            if(Objects.nonNull(requestDTO.getValue())){
                Predicate equal = criteriaBuilder.like(root.get("name"),
                        "%" +requestDTO.getValue() + "%");
                predicates.add(equal);
            }
            assert query != null;
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        Sort sort = Sort.by(pageRequestDTO.getSort(), "id");
        Pageable pageable = PageRequest.of(Integer.parseInt(pageRequestDTO
                        .getPageNo()), Integer.parseInt(pageRequestDTO.getPageSize()),
                sort);
        Page<Cities> all = cityRepository.findAll(citiesSpecification, pageable);
        return all.map(car -> GetCitiesResponse.builder()
                .name(car.getName())
                .country("Indonesia")
                .build()
        );
    }
}

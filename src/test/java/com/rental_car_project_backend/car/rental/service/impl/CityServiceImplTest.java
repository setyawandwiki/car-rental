package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchRequestDTO;
import com.rental_car_project_backend.car.rental.entity.Cities;
import com.rental_car_project_backend.car.rental.repository.CityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {
    @Mock
    private CityRepository cityRepository;
    @InjectMocks
    private CityServiceImpl cityService;
    SearchRequestDTO searchRequestDTO;
    PageRequestDTO pageRequestDTO;
    Cities cities;
    @Captor
    ArgumentCaptor<Specification<Cities>> specificationArgumentCaptor;
    @BeforeEach
    public void setup(){
        searchRequestDTO = SearchRequestDTO.builder()
                .value("cibi")
                .build();
        pageRequestDTO = PageRequestDTO.builder()
                .sort(Sort.Direction.ASC)
                .pageSize("10")
                .pageNo("0")
                .build();
    }
    @Test
    void cityServiceImpl_GetCitiesMethodShouldReturnCityAsPage() {
        // given
        Cities cities1 = Cities.builder()
                .id(1)
                .name("cibinong")
                .updatedAt(null)
                .createdAt(LocalDateTime.now())
                .build();
        Cities cities2 = Cities.builder()
                .id(2)
                .name("jakarta")
                .updatedAt(null)
                .createdAt(LocalDateTime.now())
                .build();
        Sort sort = Sort.by(pageRequestDTO.getSort(), "id");
        Pageable pageable = PageRequest.of(
                Integer.parseInt(pageRequestDTO.getPageNo()),
                Integer.parseInt(pageRequestDTO.getPageSize()),
                sort
        );
        Page<Cities> page = new PageImpl<>(List.of(cities2, cities1));
        cityRepository.saveAll(List.of(cities2, cities1));
        Mockito.when(cityRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(page);
        // when
        cityService.getCities(searchRequestDTO, pageRequestDTO);
        // then
        Mockito.verify(cityRepository).findAll(specificationArgumentCaptor.capture(), Mockito.eq(pageable));
        Specification<Cities> value = specificationArgumentCaptor.getValue();
        assertThat(cityRepository.findAll(value, pageable).getContent().size()).isEqualTo(2);
    }
}
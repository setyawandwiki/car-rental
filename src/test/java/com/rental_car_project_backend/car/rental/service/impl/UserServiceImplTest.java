package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchUserDTO;
import com.rental_car_project_backend.car.rental.dto.response.user.UserResponse;
import com.rental_car_project_backend.car.rental.entity.Address;
import com.rental_car_project_backend.car.rental.entity.Cars;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.enums.AddressStatus;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Captor
    ArgumentCaptor<Specification<Users>> usersSpecification;
    PageRequestDTO pageRequestDTO;
    SearchUserDTO searchRequestDTO;
    @BeforeEach
    void setUp(){
        searchRequestDTO = searchRequestDTO.builder()
                .email("serttyawan")
                .build();
        pageRequestDTO = PageRequestDTO.builder()
                .pageNo("0")
                .pageSize("10")
                .sort(Sort.Direction.ASC)
                .build();
    }
    @Test
    void getAllUsers_shouldSuccess(){
        // Given
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPageNo("0");
        pageRequestDTO.setPageSize("10");
        pageRequestDTO.setSort(Sort.Direction.ASC);
        pageRequestDTO.setSortColumn("id");

        SearchUserDTO searchUserDTO = new SearchUserDTO();
        // Set search criteria if needed, e.g.:
        // searchUserDTO.setEmail("dwiki");

        Users user1 = Users.builder()
                .id(1)
                .email("seryawandwiki1@gmail.com")
                .password(new BCryptPasswordEncoder().encode("dwiki123"))
                .fullName("Dwiki") // Add fields used in Specification
                .build();
        Users user2 = Users.builder()
                .id(2)
                .email("danny@gmail.com")
                .password(new BCryptPasswordEncoder().encode("danny123"))
                .fullName("Danny")
                .build();
        List<Users> users = List.of(user1, user2); // Order by id ascending
        PageImpl<Users> usersPage = new PageImpl<>(users);

        Sort sort = Sort.by(pageRequestDTO.getSort(), pageRequestDTO.getSortColumn());
        Pageable pageable = PageRequest.of(
                Integer.parseInt(pageRequestDTO.getPageNo()),
                Integer.parseInt(pageRequestDTO.getPageSize()),
                sort
        );

        // Mock the repository behavior
        Mockito.when(userRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(usersPage);

        // When
        Page<UserResponse> result = userService.getAllUsers(pageRequestDTO, searchUserDTO);

        // Then
        Mockito.verify(userRepository).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("seryawandwiki1@gmail.com");
        assertThat(result.getContent().get(1).getEmail()).isEqualTo("danny@gmail.com");
    }
}
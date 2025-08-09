package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.company.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.exceptions.IncompletableDataException;
import com.rental_car_project_backend.car.rental.exceptions.UserNotFoundException;
import com.rental_car_project_backend.car.rental.repository.CityRepository;
import com.rental_car_project_backend.car.rental.repository.CompanyRepository;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import com.rental_car_project_backend.car.rental.repository.VendorRepository;
import com.rental_car_project_backend.car.rental.service.ImageUploadService;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ImageUploadService imageUploadService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VendorRepository vendorRepository;
    @Mock
    private CityRepository cityRepository;
    @InjectMocks
    private CompanyServiceImpl companyService;
    CreateCompanyRequest request;
    @BeforeEach
    public void setUp(){
        request = CreateCompanyRequest.builder()
                .rate(3.5)
                .imageFile(new MockMultipartFile("test",
                        "car.jpg",
                        "image/jpeg",
                        "fake-image-content".getBytes()))
                .name("BMW")
                .idCity(1)
                .build();
    }
    @Test
    void companyServicImpl_CreateCompanyMethodShoudlReturnNotLogin() {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(false);
        // when
        // then
        assertThatThrownBy(()->
                companyService.createCompany(request))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("You must logged in first!");
    }

    @Test
    void companyServicImpl_CreateCompanyMethodShouldReturnUserEmailNotFound() {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.getName()).thenReturn("test");
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(userRepository.findByEmail("test")).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                companyService.createCompany(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("user not found with email " + "test");
    }

    @Test
    void companyServiceImpl_CreateCompanyMethodShouldReturnIncompletableDataException(){
        // given
        Users users = Users.builder()
                .id(1)
                .email("test@gmail.com")
                .build();
        Users spy = Mockito.spy(users);
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.getName()).thenReturn("test@gmail.com");
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(users));
        // when
        // then
        assertThatThrownBy(()->
                companyService.createCompany(request))
                .isInstanceOf(IncompletableDataException.class)
                .hasMessageContaining("User data is incomplete: " +
                        "full name, bank code, or account number is missing");
    }

    @Test
    void updateCompany() {
    }

    @Test
    void deletecompany() {
    }

    @Test
    void getCompanies() {
    }

    @Test
    void findCompany() {
    }
}
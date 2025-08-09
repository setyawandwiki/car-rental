package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.company.CreateCompanyRequest;
import com.rental_car_project_backend.car.rental.dto.request.company.UpdateCompanyRequest;
import com.rental_car_project_backend.car.rental.entity.Cities;
import com.rental_car_project_backend.car.rental.entity.Companies;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.entity.Vendor;
import com.rental_car_project_backend.car.rental.exceptions.CompanyNotFoundException;
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
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
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
    @Captor
    ArgumentCaptor<Companies> companiesArgumentCaptor;
    @Captor
    ArgumentCaptor<Vendor> vendorArgumentCaptor;
    CreateCompanyRequest request;
    UpdateCompanyRequest updateCompanyRequest;
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
        updateCompanyRequest = UpdateCompanyRequest.builder()
                .rate(3.5)
                .imageFile(new MockMultipartFile("test",
                        "car.jpg",
                        "image/jpeg",
                        "fake-image-content".getBytes()))
                .name("BMW")
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
    void companyServiceImpl_CreateCompanyMethodSucceeded() throws IOException {
        // given
        Users users = Users.builder()
                .id(1)
                .email("test@gmail.com")
                .fullName("dwiki setyawan")
                .bankCode("BCA")
                .accountNumber("123456789")
                .build();
        Companies companies = new Companies();
        companies.setCreatedAt(LocalDateTime.now());
        companies.setRate(request.getRate());
        companies.setName(request.getName());
        companies.setIdUser(users.getId());
        companies.setIdCity(request.getIdCity());
        companies.setImage(request.getImageFile().toString());
        Vendor vendor = Vendor.builder()
                .id(1)
                .companyId(1)
                .availableBalance(1000.0)
                .companyId(1)
                .build();
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.getName()).thenReturn("test@gmail.com");
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(users));
        Mockito.when(imageUploadService.uploadImage(Mockito.eq
                (request.getImageFile()), Mockito.anyString(), Mockito.eq("company-images")))
                        .thenReturn("image-upload");
        Mockito.when(companyRepository.save(Mockito.any(Companies.class))).thenReturn(companies);
        Mockito.when(vendorRepository.save(Mockito.any(Vendor.class))).thenReturn(vendor);
        // when
        companyService.createCompany(request);
        // then
        Mockito.verify(companyRepository).save(companiesArgumentCaptor.capture());
        Mockito.verify(vendorRepository).save(vendorArgumentCaptor.capture());

        Companies value = companiesArgumentCaptor.getValue();
        Vendor vendorCapture = vendorArgumentCaptor.getValue();
        assertThat(value.getName()).isEqualTo("BMW");
        /**
         * nilai vendornya diambil dari implnya
         * */
        assertThat(vendorCapture.getCompanyId()).isEqualTo(value.getId());
    }

    @Test
    void companyServiceImpl_UpdateCompanyMethodShouldReturnNotLogin() {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(false);
        // when
        // then
        assertThatThrownBy(()->
                companyService.updateCompany(1, updateCompanyRequest))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("You must logged in first!");
    }

    @Test
    void companyServiceImpl_UpdateCompanyMethodShouldReturnCompanyNotFound() {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(companyRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                companyService.updateCompany(1, updateCompanyRequest))
                .isInstanceOf(CompanyNotFoundException.class)
                .hasMessageContaining("Company not found with id " + 1);
    }

    @Test
    void companyServicImpl_UpdateCompanyMethodShouldSucceeded() throws IOException {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Companies companies = Companies.builder()
                .image(updateCompanyRequest.getImageFile().toString())
                .rate(updateCompanyRequest.getRate())
                .name(updateCompanyRequest.getName())
                .updatedAt(LocalDateTime.now())
                .build();
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(companyRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(companies));
        Mockito.when(imageUploadService.uploadImage(Mockito.eq(updateCompanyRequest.getImageFile()),
                Mockito.anyString(), Mockito.eq("company-images"))).thenReturn("image-url");
        Mockito.when(companyRepository.save(companies)).thenReturn(companies);
        // when
        companyService.updateCompany(1, updateCompanyRequest);
        // then
        Mockito.verify(companyRepository).save(companiesArgumentCaptor.capture());
        Companies value = companiesArgumentCaptor.getValue();
        assertThat(value.getName()).isEqualTo(updateCompanyRequest.getName());
    }


    @Test
    void companyServicImpl_DeleteMethodShouldReturnYouMustLoginFirst() throws IOException {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(false);
        // when
        // then
        assertThatThrownBy(()->
                companyService.deletecompany(1))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("You must logged in first!");
    }

    @Test
    void companyServicImpl_DeleteMethodShouldReturnCompanyIdNotFound() throws IOException {
        // given
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(companyRepository.findById(1)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                companyService.deletecompany(1))
                .isInstanceOf(CompanyNotFoundException.class)
                .hasMessageContaining("Company not found with id " + 1);
    }

    @Test
    void companyServiceImpl_DeleteMethodShouldSucceeded() throws IOException {
        // given
        Companies companies = Companies.builder()
                .id(1)
                .image(updateCompanyRequest.getImageFile().toString())
                .rate(updateCompanyRequest.getRate())
                .name(updateCompanyRequest.getName())
                .updatedAt(LocalDateTime.now())
                .build();
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        SecurityContext fakeContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(fakeContext);
        Mockito.when(fakeContext.getAuthentication()).thenReturn(fakeAuthentication);
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(companyRepository.findById(1)).thenReturn(Optional.of(companies));
        Mockito.doNothing().when(imageUploadService)
                .deleteImage(Mockito.anyString(), Mockito.eq("company-images"));
        // when
        companyService.deletecompany(1);
        // then
        Mockito.verify(companyRepository).delete(companies);
    }

    @Test
    void companyServiceImpl_GetAllCompanies() {
        // given
        Companies companies = Companies.builder()
                .id(1)
                .image(updateCompanyRequest.getImageFile().toString())
                .rate(updateCompanyRequest.getRate())
                .name(updateCompanyRequest.getName())
                .updatedAt(LocalDateTime.now())
                .build();
        Companies companies2 = Companies.builder()
                .id(1)
                .image(updateCompanyRequest.getImageFile().toString())
                .rate(updateCompanyRequest.getRate())
                .name(updateCompanyRequest.getName())
                .updatedAt(LocalDateTime.now())
                .build();
        Mockito.when(companyRepository.findAll())
                .thenReturn(List.of(companies2, companies));
        // when
        companyService.getCompanies();
        // then
        Mockito.verify(companyRepository).findAll();
        assertThat(companyRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void companyServiceImpl_GetCompanyNotFound() {
        // given
        Mockito.when(companyRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                companyService.findCompany(1))
                .isInstanceOf(CompanyNotFoundException.class)
                .hasMessageContaining("Company not found with id " + 1);
    }

    @Test
    void companyServiceImpl_GetCompanySucceeded() {
        // given
        Companies companies = Companies.builder()
                .id(1)
                .idCity(1)
                .image(updateCompanyRequest.getImageFile().toString())
                .rate(updateCompanyRequest.getRate())
                .name(updateCompanyRequest.getName())
                .updatedAt(LocalDateTime.now())
                .build();
        Cities cities = Cities.builder()
                .id(1)
                .name("cibinong")
                .build();
        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.of(cities));
        Mockito.when(companyRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(companies));
        // when
        companyService.findCompany(1);
        // then
        Mockito.verify(companyRepository).findById(1);
    }
}
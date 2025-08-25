package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.user.LoginRequest;
import com.rental_car_project_backend.car.rental.dto.request.user.RegisterRequest;
import com.rental_car_project_backend.car.rental.dto.response.user.LoginResponse;
import com.rental_car_project_backend.car.rental.dto.response.user.RegisterResponse;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer
            = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.2"));

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void canEstablishedConnectionInHere(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @BeforeEach
    void setupUser() {
        RegisterRequest request = RegisterRequest.builder()
                .password("dwiki123")
                .email("setyawandwiki1@gmail.com")
                .build();

        testRestTemplate.exchange(
                "/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request),
                RegisterResponse.class
        );
    }

    @Test
    void register_WithValidData_ShouldSucceed() {
        // given
        RegisterRequest request = RegisterRequest.builder()
                .password("dwiki123")
                .email("setyawandwiki2@gmail.com")
                .build();

        // when
        ResponseEntity<RegisterResponse> response = testRestTemplate.exchange(
                "/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request),
                RegisterResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("setyawandwiki2@gmail.com");
    }

    @Test
    void login_WithValidData_ShouldSucceed() {
        LoginRequest loginRequest = LoginRequest.builder()
                .password("dwiki123")
                .email("setyawandwiki1@gmail.com")
                .build();

        ResponseEntity<LoginResponse> loginResponse = testRestTemplate.exchange(
                "/auth/login",
                HttpMethod.POST,
                new HttpEntity<>(loginRequest),
                LoginResponse.class
        );

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        assertThat(loginResponse.getBody().getMessage()).isEqualTo("Login Successful");
    }
}
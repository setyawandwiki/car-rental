package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.user.RegisterRequest;
import com.rental_car_project_backend.car.rental.dto.response.user.RegisterResponse;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
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

    @Test
    void register_ShouldRegister() {
        // given
        RegisterRequest request = RegisterRequest.builder()
                .password("dwiki123")
                .email("setyawandwiki1@gmail.com")
                .build();

        // when
        ResponseEntity<RegisterResponse> response = testRestTemplate.exchange(
                "/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request),
                RegisterResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("setyawandwiki1@gmail.com");
    }

    @Test
    void login() {
    }
}
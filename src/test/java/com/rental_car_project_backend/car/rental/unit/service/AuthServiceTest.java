package com.rental_car_project_backend.car.rental.unit.service;

import com.rental_car_project_backend.car.rental.dto.request.user.RegisterRequest;
import com.rental_car_project_backend.car.rental.dto.response.user.RegisterResponse;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.repository.RoleRepository;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import com.rental_car_project_backend.car.rental.service.AuthService;
import com.rental_car_project_backend.car.rental.service.JWTService;
import com.rental_car_project_backend.car.rental.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private  AuthenticationManager authenticationManager;
    @Mock
    private  JWTService jwtService;
    @Mock
    private RoleRepository roleRepository;
    @Captor
    ArgumentCaptor<Users> usersArgumentCaptor;
    @Mock
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    @InjectMocks
    AuthServiceImpl authServiceImpl;

/*    @BeforeEach
    void setUp(){
        authServiceImpl = new AuthServiceImpl()
    }*/

    @Test
    public void authService_SuccessRegister(){
        // given
        RegisterRequest request = RegisterRequest.builder()
                .email("setyawandwiki1@gmail.com")
                .password("123456")
                .build();
        Mockito.when(bCryptPasswordEncoder.encode("123456")).thenReturn(Mockito.anyString());
        // when
        authServiceImpl.register(request);
        // then
        Mockito.verify(userRepository).save(usersArgumentCaptor.capture());
        Mockito.verify(bCryptPasswordEncoder).encode("123456");
        Users value = usersArgumentCaptor.getValue();
        assertThat(value.getEmail()).isEqualTo(request.getEmail());
        assertThat(value.getPassword()).isNotEqualTo(request.getPassword());
    }
}
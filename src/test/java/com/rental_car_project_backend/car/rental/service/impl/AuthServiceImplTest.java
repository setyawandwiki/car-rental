package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.user.RegisterRequest;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.repository.RoleRepository;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import com.rental_car_project_backend.car.rental.service.JWTService;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testcontainers.shaded.com.trilead.ssh2.auth.AuthenticationManager;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTService jwtService;
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
        Users fakeUser = new Users();
        Mockito.when(bCryptPasswordEncoder.encode("123456")).thenReturn(Mockito.anyString());
        fakeUser.setId(1);
        fakeUser.setEmail(request.getEmail());
        fakeUser.setPassword(bCryptPasswordEncoder.encode("123456"));
        Mockito.when(userRepository.save(Mockito.any(Users.class))).thenReturn(fakeUser);
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
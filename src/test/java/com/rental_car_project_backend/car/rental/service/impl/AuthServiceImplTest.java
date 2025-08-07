package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.user.LoginRequest;
import com.rental_car_project_backend.car.rental.dto.request.user.RegisterRequest;
import com.rental_car_project_backend.car.rental.dto.response.user.RegisterResponse;
import com.rental_car_project_backend.car.rental.entity.Roles;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.exceptions.RoleNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.UserNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.UsernameAndPasswordInvalidException;
import com.rental_car_project_backend.car.rental.repository.RoleRepository;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import com.rental_car_project_backend.car.rental.service.JWTService;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

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
    @Captor
    ArgumentCaptor<Authentication> authenticationArgumentCaptor;
    @Mock
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    @InjectMocks
    AuthServiceImpl authServiceImpl;

    RegisterRequest request;
    LoginRequest loginRequest;
    RegisterResponse registerResponse;

    @BeforeEach
    void setUp(){
        request = RegisterRequest.builder()
                .email("setyawandwiki1@gmail.com")
                .password("123456")
                .build();

        loginRequest = LoginRequest.builder()
                .email("setyawandwiki1@gmail.com")
                .password("123456")
                .build();
    }

    @Test
    public void authService_SuccessRegister(){
        // given
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

    @Test
    public void authService_shouldReturnUserNotFound(){
        // given
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword()));
        Mockito.when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                authServiceImpl.login(loginRequest))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    public void authService_shouldReturnRoleRepositoryNotFound(){
        // given
        Users fakeUser = Users.builder()
                .id(5)
                .idRole(1)
                .email("setyawandwiki1@gmail.com")
                .password(bCryptPasswordEncoder.encode("123456"))
                .build();
        Authentication authenticate = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        Mockito.when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(fakeUser));
        Mockito.when(roleRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(()->
                authServiceImpl.login(loginRequest))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining("Role not found with id role : " + fakeUser.getIdRole());
    }
    @Test
    public void authService_shouldReturnUsernameAndPasswordInvalidException(){
        // given
        Users fakeUser = Users.builder()
                .id(5)
                .idRole(1)
                .email("setyawandwiki1@gmail.com")
                .password(bCryptPasswordEncoder.encode("123456"))
                .build();
        Roles fakeRole = Roles.builder()
                .id(1)
                .name("USER")
                .build();
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(fakeAuthentication);
        Mockito.when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(fakeUser));
        Mockito.when(roleRepository.findById(fakeUser.getIdRole()))
                .thenReturn(Optional.of(fakeRole));
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(false);
        // when
        // then
        assertThatThrownBy(()->
                authServiceImpl.login(loginRequest))
                .isInstanceOf(UsernameAndPasswordInvalidException.class)
                .hasMessageContaining("Invalid email or password");
    }

    @Test
    public void authService_shouldSuccessLogin(){
        // given
        Users fakeUser = Users.builder()
                .id(5)
                .idRole(1)
                .email("setyawandwiki1@gmail.com")
                .password(bCryptPasswordEncoder.encode("123456"))
                .build();
        Roles fakeRole = Roles.builder()
                .id(1)
                .name("USER")
                .build();
        Authentication fakeAuthentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(fakeAuthentication);
        Mockito.when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(fakeUser));
        Mockito.when(roleRepository.findById(fakeUser.getIdRole()))
                .thenReturn(Optional.of(fakeRole));
        Mockito.when(fakeAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(jwtService.generateToken(request.getEmail())).thenReturn(Mockito.any(String.class));
        // when
        authServiceImpl.login(loginRequest);
        // then
        Mockito.verify(authenticationManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(loginRequest.getEmail());
        Mockito.verify(roleRepository).findById(fakeUser.getIdRole());
        Mockito.verify(jwtService, Mockito.atLeastOnce()).generateToken(loginRequest.getEmail());
    }
}
package com.rental_car_project_backend.car.rental.config;

import com.rental_car_project_backend.car.rental.entity.UserPrincipal;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.exceptions.UserNotFoundException;
import com.rental_car_project_backend.car.rental.repository.RoleRepository;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(username).orElseThrow(() ->{
          throw new UserNotFoundException("User not found with email " + username);
        });
        String name = roleRepository.findById(users.getIdRole()).orElseThrow(() ->
                new RuntimeException("role not found")).getName();
        return new UserPrincipal(users, name);
    }
}

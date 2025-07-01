package com.rental_car_project_backend.car.rental.config;

import com.rental_car_project_backend.car.rental.global.ExceptionFilterHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CustomUserDetailService customUserDetailService;
    private final ExceptionFilterHandler exceptionFilterHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JWTAuthenticationEntryPointHandler jwtAuthenticationEntryPointHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPointHandler)
                                .accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/auth/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/car")
                                .hasAnyAuthority("ADMIN", "SUPERADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/car/**")
                                .hasAnyAuthority("ADMIN","SUPERADMIN")
                                .requestMatchers(HttpMethod.PUT,"/car/**")
                                .hasAnyAuthority("SUPERADMIN","ADMIN")
                                .requestMatchers(HttpMethod.POST, "/car")
                                .hasAnyAuthority("ADMIN", "SUPERADMIN")
                                .requestMatchers(HttpMethod.GET, "/car/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/company-car")
                                .hasAnyAuthority("ADMIN", "SUPERADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/company-car/**")
                                .hasAnyAuthority("ADMIN","SUPERADMIN")
                                .requestMatchers(HttpMethod.PUT,"/company-car/**")
                                .hasAnyAuthority("SUPERADMIN","ADMIN")
                                .requestMatchers(HttpMethod.POST, "/company-car")
                                .hasAnyAuthority("ADMIN", "SUPERADMIN")
                                .requestMatchers(HttpMethod.GET, "/company-car/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionFilterHandler, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return  daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}

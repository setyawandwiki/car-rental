package com.rental_car_project_backend.car.rental.config;

import com.rental_car_project_backend.car.rental.global.ExceptionFilterHandler;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

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
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();
                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5713"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);
                                return configuration;
                            }
                        }))
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPointHandler)
                                .accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/auth/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/users")
                                .hasAnyAuthority("SUPER_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users/update")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
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
                                .hasAnyAuthority("ADMIN", "SUPERADMIN")
                                .requestMatchers(HttpMethod.GET, "/company-car")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/orders")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.DELETE,"/orders/**")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.GET, "/orders/**")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.GET, "/orders")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.POST, "/payments")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.DELETE,"/payments/**")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.GET, "/payments/**")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.GET, "/payments")
                                .hasAnyAuthority("USER")
                                .requestMatchers("/payments/webhook/**").permitAll()
                                .anyRequest()
                                .permitAll())
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

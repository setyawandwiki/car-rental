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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPointHandler)
                                .accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/auth/**","/v3/api-docs/**", "/swagger-ui/**")
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
                                .requestMatchers(HttpMethod.POST, "/company")
                                .hasAnyAuthority("ADMIN", "SUPERADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/company/**")
                                .hasAnyAuthority("ADMIN","SUPERADMIN")
                                .requestMatchers(HttpMethod.PUT,"/company/**")
                                .hasAnyAuthority("SUPERADMIN","ADMIN")
                                .requestMatchers(HttpMethod.POST, "/company")
                                .hasAnyAuthority("ADMIN", "SUPERADMIN")
                                .requestMatchers(HttpMethod.GET, "/company/**")
                                .hasAnyAuthority("SUPERADMIN","ADMIN")
                                .requestMatchers(HttpMethod.POST, "/company-car")
                                .hasAnyAuthority("ADMIN", "SUPERADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/company-car/**")
                                .hasAnyAuthority("ADMIN","SUPERADMIN")
                                .requestMatchers(HttpMethod.PUT,"/company-car/**")
                                .hasAnyAuthority("SUPERADMIN","ADMIN")
                                .requestMatchers(HttpMethod.POST, "/company-car")
                                .hasAnyAuthority("ADMIN", "SUPERADMIN")
                                .requestMatchers(HttpMethod.GET, "/company-car/**")
                                .hasAnyAuthority("USER","ADMIN", "SUPERADMIN")
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
                                .requestMatchers("/city").permitAll()
                                .anyRequest()
                                .permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(exceptionFilterHandler, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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

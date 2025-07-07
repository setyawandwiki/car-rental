package com.rental_car_project_backend.car.rental.config;

import com.xendit.Xendit;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XenditConfig {
    @Value("${xendit.api-key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Xendit.apiKey = apiKey;
        System.out.println("✅ Xendit API key configured");
    }
}

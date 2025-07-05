package com.rental_car_project_backend.car.rental.config;

import com.xendit.XenditClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XenditConfig {
    @Value("${xendit.api-key}")
    private String apiKey;
    @Bean
    XenditClient config(){
        return new XenditClient.Builder()
                .setApikey(apiKey)
                .build();
    }
}

package com.rental_car_project_backend.car.rental;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
public class HelloController {
    @GetMapping
    public String test(){
        return "hello controller";
    }
}

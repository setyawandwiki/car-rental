package com.rental_car_project_backend.car.rental.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUploadService {
    String uploadImage(MultipartFile file) throws IOException;
}

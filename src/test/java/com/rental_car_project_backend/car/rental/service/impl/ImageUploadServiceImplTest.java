package com.rental_car_project_backend.car.rental.service.impl;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageUploadServiceImplTest {
    private MultipartFile file;
    private String publicId;
    private String location;
    private String contentType;
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");
    private static final long MAX_FILE_SIZE_KB = 1024;

    @InjectMocks
    ImageUploadServiceImpl imageUploadService;

    @BeforeEach
    public void setUp(){
        file = new MockMultipartFile("test",
                "car.jpg",
                "image/jpeg",
                "fake-image-content".getBytes());
        publicId = "1235";
        location = "image-uploads";
        contentType = file.getContentType();
    }

    @Test
    void imageUpload_UploadImageServiceIfContentTypeNullShouldReturnIllegalArgumentException() throws IOException {
        // given
        file = new MockMultipartFile("test", new byte[]{});
        // when
        // then
        assertThatThrownBy(()->
                imageUploadService.uploadImage(file, publicId, location))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("File harus berupa gambar JPG atau PNG");
    }

    @Test
    void deleteImage() {
    }
}
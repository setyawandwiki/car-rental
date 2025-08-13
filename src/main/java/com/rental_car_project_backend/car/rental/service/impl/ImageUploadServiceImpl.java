package com.rental_car_project_backend.car.rental.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rental_car_project_backend.car.rental.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {
    private final Cloudinary cloudinary;

    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");
    private static final long MAX_FILE_SIZE_KB = 1024;

    @Override
    @Transactional
    public String uploadImage(MultipartFile file, String publicId, String location) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("File harus berupa gambar JPG atau PNG");
        }

        long fileSizeKB = file.getSize() / 1024;
        if (fileSizeKB > MAX_FILE_SIZE_KB) {
            throw new IllegalArgumentException("Ukuran maksimal gambar adalah 1MB");
        }

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", location,
                "public_id", publicId,
                "overwrite", true
        ));
        return (String) uploadResult.get("secure_url");
    }

    @Override
    @Transactional
    public void deleteImage(String image, String location) throws IOException {
        cloudinary.uploader().destroy(location + "/"+image, ObjectUtils.emptyMap());
    }
}

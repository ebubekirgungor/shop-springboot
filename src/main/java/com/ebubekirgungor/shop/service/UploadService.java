package com.ebubekirgungor.shop.service;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {
    public void upload(MultipartFile image, String location) {
        try {
            Files.copy(image.getInputStream(), Paths.get(location).resolve(image.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not upload the image: " + e.getMessage());
        }
    }
}

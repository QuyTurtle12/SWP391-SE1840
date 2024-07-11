package com.swp391.jewelrysalesystem.controllers;

import com.google.firebase.cloud.StorageClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @PostMapping("/image")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
            }

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String contentType = file.getContentType();

            // Upload file to Firebase Storage
            String bucketName = "swp391-c9c7c.appspot.com";
            String filePath = "uploads/" + fileName;
            StorageClient.getInstance().bucket(bucketName).create(filePath, file.getBytes(), contentType);

            // Get the public URL
            String fileUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                    URLEncoder.encode(bucketName, StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString()));

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }
}

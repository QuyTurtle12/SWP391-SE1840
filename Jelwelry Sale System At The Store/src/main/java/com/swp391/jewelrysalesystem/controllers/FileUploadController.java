package com.swp391.jewelrysalesystem.controllers;

import com.google.firebase.cloud.StorageClient;
import org.apache.http.HttpStatus;
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
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String contentType = file.getContentType();

            // Upload file to Firebase Storage
            StorageClient.getInstance().bucket().create("uploads/" + fileName, file.getBytes(), contentType);

            // Get the public URL
            String fileUrl = String.format("https://firebasestorage.googleapis.com/v0/b/swp391-c9c7c.appspot.com/o/%s?alt=media",
                    URLEncoder.encode("uploads/" + fileName, StandardCharsets.UTF_8.toString()));

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }
}

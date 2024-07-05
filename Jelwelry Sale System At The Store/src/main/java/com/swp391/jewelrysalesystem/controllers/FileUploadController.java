package com.swp391.jewelrysalesystem.controllers;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

            // Get a reference to the Firebase Storage bucket
            Storage storage = StorageOptions.getDefaultInstance().getService();
            String bucketName = "swp391-c9c7c.appspot.com";

            // Create a blob ID based on the file name
            BlobId blobId = BlobId.of(bucketName, "uploads/" + fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

            // Upload the file to Firebase Storage
            storage.create(blobInfo, file.getBytes());

            // Get the public URL
            String fileUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                    URLEncoder.encode(bucketName, StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode("uploads/" + fileName, StandardCharsets.UTF_8.toString()));

            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }
}
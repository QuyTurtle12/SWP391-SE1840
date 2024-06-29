package com.swp391.jewelrysalesystem.config;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() throws IOException {
        try (InputStream serviceAccountStream = getClass().getClassLoader()
                .getResourceAsStream("swp391-c9c7c-firebase-adminsdk-wnrge-dc00425ac1.json")) {
            if (serviceAccountStream == null) {
                throw new IllegalArgumentException("Firebase service account key file not found.");
            }
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();
            FirebaseApp.initializeApp(options);
        }
    }
}

package com.amorgakco.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FcmInitializer {

    private final String secretPath;

    public FcmInitializer(@Value("${fcm-secret-path}") final String secretPath) {
        this.secretPath = secretPath;
    }

    @PostConstruct
    public void initialize() throws IOException {
        final GoogleCredentials googleCredentials =
                GoogleCredentials.fromStream(new ClassPathResource(secretPath).getInputStream());
        final FirebaseOptions options =
                FirebaseOptions.builder().setCredentials(googleCredentials).build();
        FirebaseApp.initializeApp(options);
    }
}

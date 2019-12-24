package com.example.oauth.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * OAuth client application.
 */
@EnableScheduling
@SpringBootApplication
public class OauthClientApplication {

    /**
     * Entry point to the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(OauthClientApplication.class, args);
    }
}

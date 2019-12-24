package com.example.oauth.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OAuth resource server application.
 */
@SpringBootApplication
public class OAuthResourceServerApplication {

    /**
     * Entry point to the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(OAuthResourceServerApplication.class, args);
    }
}

package com.example.oauth.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OAuth API Gateway application.
 */
@SpringBootApplication
public class ApiGatewayApplication {

    /**
     * Entry point of the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

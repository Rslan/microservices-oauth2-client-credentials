package com.example.oauth.apigateway.util;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.Optional;

import org.springframework.web.server.ServerWebExchange;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class exposing functionality to work with HTTP entities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpUtils {

    public static final String BEARER_TYPE = "Bearer";

    static final String BASIC_AUTH = "Basic";

    /**
     * Get an Authorization header from an HTTP request.
     *
     * @param exchange {@link ServerWebExchange} containing and HTTP request to get the Authorization token from
     * @return an optional representing a value of Authorization header or an empty optional if the request does not
     * contain the header
     */
    public static Optional<String> getAuthorizationHeader(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getRequest()
                                           .getHeaders()
                                           .get(AUTHORIZATION))
                       .map(strings -> strings.get(0));
    }

    /**
     * Extracts encoded basic credentials from the value of Authorization header.
     *
     * @param authorization value of Authorization header
     * @return Base64 encoded value of credentials
     */
    public static String extractCredentialsOutOfBasicAuthorizationHeader(String authorization) {
        return authorization.substring(BASIC_AUTH.length() + 1);
    }
}

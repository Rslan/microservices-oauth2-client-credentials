package com.example.oauth.apigateway.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HttpUtils} class.
 */
class HttpUtilsTest {

    @Test
    public void extractsCredentialsOutOfBasicAuthorizationHeader() {
        String authorization = HttpUtils.BASIC_AUTH + " MyCreDentIALs";

        assertEquals("MyCreDentIALs", HttpUtils.extractCredentialsOutOfBasicAuthorizationHeader(authorization));
    }
}

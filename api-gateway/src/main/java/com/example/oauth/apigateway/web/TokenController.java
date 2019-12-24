package com.example.oauth.apigateway.web;

import static com.example.oauth.apigateway.util.HttpUtils.BEARER_TYPE;
import static com.example.oauth.apigateway.util.HttpUtils.extractCredentialsOutOfBasicAuthorizationHeader;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth.apigateway.storage.TokenCacheService;
import com.example.oauth.apigateway.model.AccessTokenResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * REST controller providing endpoints for OAuth token manipulation.
 */
@Slf4j
@RestController
@RequestMapping(path = "/oauth/token", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenController {

    private final TokenCacheService tokenService;

    /**
     * Constructs an instance of the controller.
     *
     * @param tokenService instance of the service working with the token caching
     */
    public TokenController(TokenCacheService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Gets OAuth token from cache.
     *
     * @param authorization value of Authorization header identifying the OAuth token to get from cache
     * @return a POJO containing the cached access token
     */
    @PostMapping("/cached")
    Mono<AccessTokenResponse> retrieveCachedToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String token = tokenService.get(extractCredentialsOutOfBasicAuthorizationHeader(authorization));
        log.debug("Returning cached access token for authorization {}", authorization);

        AccessTokenResponse response = new AccessTokenResponse();
        response.setAccessToken(token);
        response.setTokenType(BEARER_TYPE);

        return Mono.just(response);
    }
}

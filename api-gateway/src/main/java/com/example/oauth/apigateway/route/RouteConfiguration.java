package com.example.oauth.apigateway.route;

import static com.example.oauth.apigateway.util.HttpUtils.extractCredentialsOutOfBasicAuthorizationHeader;
import static java.util.function.Predicate.not;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import com.example.oauth.apigateway.model.AccessTokenResponse;
import com.example.oauth.apigateway.storage.RedisConfiguration;
import com.example.oauth.apigateway.storage.TokenCacheService;
import com.example.oauth.apigateway.util.HttpUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * A configuration of API Gateway describing where the client requests should be routed, which filters should be applied
 * to the requests etc.
 */
@Slf4j
@Configuration
@Import(RedisConfiguration.class)
public class RouteConfiguration {

    private final TokenCacheService redisTokenService;

    @Value("${spring.security.oauth2.provider.auth0.audience}")
    private String auth0Audience;
    @Value("${spring.security.oauth2.provider.auth0.token-uri}")
    private String auth0TokenUri;
    @Value("${api-gateway.oauth-token.path}")
    private String oauthTokenPath;
    @Value("${api-gateway.oauth-token.cached.path}")
    private String oauthTokenCachedPath;
    @Value("${api-gateway.uri}")
    private String apiGatewayUri;
    @Value("${api-gateway.oauth-token.expiration-skew-sec}")
    private int tokenExpirationSkewSec;

    public RouteConfiguration(TokenCacheService redisTokenService) {
        this.redisTokenService = redisTokenService;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                      .route("new-oauth-token", r ->
                              r.path(oauthTokenPath)
                               .and()
                               .method(HttpMethod.POST)
                               .and()
                               .predicate(not(this::isTokenInCache))
                               .filters(createNewOAuthTokenRouteFilters())
                               .uri(auth0TokenUri))
                      .route("cached-oauth-token", r ->
                              r.path(oauthTokenPath)
                               .and()
                               .method(HttpMethod.POST)
                               .and()
                               .predicate(this::isTokenInCache)
                               .filters(createCachedOAuthTokenRouteFilters())
                               .uri(apiGatewayUri))
                      .build();
    }

    private boolean isTokenInCache(ServerWebExchange exchange) {
        return HttpUtils.getAuthorizationHeader(exchange)
                        .map(authorization -> redisTokenService.get(
                                extractCredentialsOutOfBasicAuthorizationHeader(authorization)))
                        .isPresent();
    }

    private Function<GatewayFilterSpec, UriSpec> createNewOAuthTokenRouteFilters() {
        return f -> f.modifyRequestBody(MultiValueMap.class, MultiValueMap.class,
                                        MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                                        (exchange, formFields) -> {
                                            formFields.add("audience", auth0Audience);
                                            return Mono.just(formFields);
                                        })
                     .modifyResponseBody(AccessTokenResponse.class, AccessTokenResponse.class,
                                         MediaType.APPLICATION_JSON_VALUE,
                                         (exchange, accessTokenResponse) -> {
                                             cacheAccessTokenByOAuthClientId(exchange, accessTokenResponse);
                                             return Mono.just(accessTokenResponse);
                                         });
    }

    private Function<GatewayFilterSpec, UriSpec> createCachedOAuthTokenRouteFilters() {
        return f -> f.rewritePath(oauthTokenPath, oauthTokenPath + oauthTokenCachedPath);
    }

    private void cacheAccessTokenByOAuthClientId(ServerWebExchange exchange, AccessTokenResponse accessTokenResponse) {
        HttpUtils.getAuthorizationHeader(exchange)
                 .ifPresentOrElse(
                         authorization -> redisTokenService.put(
                                 extractCredentialsOutOfBasicAuthorizationHeader(authorization),
                                 accessTokenResponse.getAccessToken(),
                                 accessTokenResponse.getExpiresIn() - tokenExpirationSkewSec),
                         () -> log.info("Failed to get value of '{}' header in request", AUTHORIZATION));
    }
}

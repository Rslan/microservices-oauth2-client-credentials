package com.example.oauth.client.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * An implementation of {@link ClientService} using Spring WebClient to make HTTP calls.
 */
@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    private final WebClient webClient;

    @Value("${oauth-resource.uri.public}")
    private String oauthResourceUriPublic;
    @Value("${oauth-resource.uri.private}")
    private String oauthResourceUriPrivate;
    @Value("${oauth-resource.uri.private-scoped}")
    private String oauthResourceUriPrivateScoped;
    @Value("${request.timeout.seconds}")
    private int requestTimeoutSeconds;

    /**
     * Constructs an instance of the service.
     *
     * @param webClient configured {@link WebClient} bean used to perform HTTP calls
     */
    public ClientServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Periodically call a resource server's endpoints.
     */
    @Scheduled(fixedRate = 60000)
    public void performRequestsToResourceServer() {
        callServer(oauthResourceUriPublic);
        callServer(oauthResourceUriPrivate);
        callServer(oauthResourceUriPrivateScoped);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void callServer(String endpointHttpUrl) {
        String response = webClient.get()
                                   .uri(UriComponentsBuilder.fromHttpUrl(endpointHttpUrl)
                                                            .toUriString())
                                   .retrieve()
                                   .bodyToMono(String.class)
                                   .block(Duration.ofSeconds(requestTimeoutSeconds));
        log.info("Response from the server: {}", response);
    }
}

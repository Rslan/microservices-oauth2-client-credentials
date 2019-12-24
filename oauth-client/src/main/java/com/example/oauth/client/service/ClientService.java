package com.example.oauth.client.service;

/**
 * OAuth client service.
 */
public interface ClientService {

    /**
     * Makes a call to the specified endpoint.
     *
     * @param endpointUri an URI to make a call to
     */
    void callServer(String endpointUri);

}

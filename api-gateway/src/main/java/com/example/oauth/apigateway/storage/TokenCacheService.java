package com.example.oauth.apigateway.storage;

/**
 * A service providing functionality for caching OAuth tokens.
 */
public interface TokenCacheService {

    /**
     * Get OAuth token from cache by its key.
     *
     * @param key a key to search for in the cache
     * @return a token stored in cache
     */
    String get(String key);

    /**
     * Put OAuth token into cache with specifying an expiration period
     * after which the value will be evicted from the cache.
     *
     * @param key           a key identifying the token to be cached
     * @param value         the token value
     * @param expirationSec time in seconds after which the token is deleted from the cache
     */
    void put(String key, String value, int expirationSec);
}

package com.example.oauth.apigateway.storage;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * An implementation of {@link TokenCacheService} that uses Redis as a cache and stores the OAuth tokens in a
 * string representation.
 */
@Service
public class StringRedisTokenCacheService implements TokenCacheService {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * Constructs an instance of the service.
     *
     * @param stringRedisTemplate redis template bean
     */
    public StringRedisTokenCacheService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue()
                                  .get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(String key, String value, int expirationSec) {
        stringRedisTemplate.opsForValue()
                           .set(key, value, expirationSec, TimeUnit.SECONDS);
    }
}

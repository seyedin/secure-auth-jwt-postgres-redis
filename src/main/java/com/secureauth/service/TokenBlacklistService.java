package com.secureauth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Manages blacklisted JWT tokens using Redis.
 */
@Service
public class TokenBlacklistService {

    private static final Logger log = LoggerFactory.getLogger(TokenBlacklistService.class);
    private final RedisTemplate<String, String> redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklisted:";

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Adds a token to the blacklist with an expiration time.
     *
     * @param token the JWT token to blacklist
     */
    public void blacklistToken(String token) {
        try {
            log.info("Blacklisting token: {}", token);
            redisTemplate.opsForValue().set(
                    BLACKLIST_PREFIX + token,
                    "true",
                    86400000,
                    TimeUnit.MILLISECONDS
            );
            log.info("Token blacklisted successfully");
        } catch (Exception e) {
            log.error("Failed to blacklist token: {}", e.getMessage());
            throw new RuntimeException("Failed to blacklist token", e);
        }
    }

    /**
     * Checks if a token is blacklisted.
     *
     * @param token the JWT token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isTokenBlacklisted(String token) {
        try {
            log.debug("Checking if token is blacklisted: {}", token);
            Boolean exists = redisTemplate.hasKey(BLACKLIST_PREFIX + token);
            log.debug("Token blacklisted: {}", exists != null && exists);
            return exists != null && exists;
        } catch (Exception e) {
            log.error("Error checking token blacklist: {}", e.getMessage());
            throw new RuntimeException("Failed to check blacklist", e);
        }
    }
}
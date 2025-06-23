package com.secureauth.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the response containing a JWT token.
 */
@Getter
@Setter
public class AuthResponse {

    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}
package com.secureauth.controller;

import com.secureauth.dto.AuthRequest;
import com.secureauth.dto.AuthResponse;
import com.secureauth.entity.User;
import com.secureauth.security.JwtTokenProvider;
import com.secureauth.service.TokenBlacklistService;
import com.secureauth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Handles authentication-related HTTP requests.
 */
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(UserService userService,
                          JwtTokenProvider jwtTokenProvider,
                          TokenBlacklistService tokenBlacklistService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    /**
     * Registers a new user and returns a JWT token.
     *
     * @param request the signup request containing username and password
     * @return the JWT token
     */
    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody AuthRequest request) {
        log.info("Processing signUp for username: {}", request.getUsername());
        User user = userService.signup(request.getUsername(), request.getPassword());
        String token = jwtTokenProvider.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request the signin request containing username and password
     * @return the JWT token
     */
    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody AuthRequest request) {
        log.info("Processing signIn for username: {}", request.getUsername());
        User user = userService.signIn(request.getUsername(), request.getPassword());
        String token = jwtTokenProvider.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Logs out the user by blacklisting their JWT token.
     *
     * @param request the HTTP request containing the Authorization header
     * @return a success message
     */
    @PostMapping("/signOut")
    public ResponseEntity<String> signOut(HttpServletRequest request) {
        log.info("Processing signOut request");
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
            log.info("Token blacklisted successfully");
        }
        return ResponseEntity.ok("Successfully logged out");
    }
}
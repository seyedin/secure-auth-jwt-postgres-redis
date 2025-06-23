package com.secureauth.exception;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Global exception handler for centralized error handling.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles JWT-related exceptions.
     *
     * @param ex the JwtException
     * @return an error response with 401 status
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtException(JwtException ex) {
        log.error("JWT error: {}", ex.getMessage());
        ErrorResponseDto responseDto = new ErrorResponseDto(ErrorCode.UNAUTHORIZED.getCode(), "Invalid or expired token");
        return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles authentication exceptions.
     *
     * @param ex the AuthenticationException
     * @return an error response with 401 status
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex) {
        log.error("Authentication error: {}", ex.getMessage());
        ErrorResponseDto responseDto = new ErrorResponseDto(ErrorCode.UNAUTHORIZED.getCode(), "Authentication failed");
        return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles authorization denied exceptions.
     *
     * @param ex the AuthorizationDeniedException
     * @return an error response with 403 status
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        log.error("Authorization denied: {}", ex.getMessage());
        ErrorResponseDto responseDto = new ErrorResponseDto(ErrorCode.FORBIDDEN.getCode(), "Access denied");
        return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles custom application exceptions.
     *
     * @param ex the CustomException
     * @return an error response with the specified error code and message
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException ex) {
        log.error("Custom exception: {}", ex.getMessage());
        ErrorResponseDto responseDto = new ErrorResponseDto(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles validation exceptions for invalid request bodies.
     *
     * @param ex the MethodArgumentNotValidException
     * @return an error response with 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation error: {}", errorMessage);
        ErrorResponseDto responseDto = new ErrorResponseDto(ErrorCode.BAD_REQUEST.getCode(), errorMessage);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles missing request parameter exceptions.
     *
     * @param ex the MissingServletRequestParameterException
     * @return an error response with 400 status
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingParams(MissingServletRequestParameterException ex) {
        log.warn("Missing parameter: {}", ex.getParameterName());
        String message = "Missing required parameter: " + ex.getParameterName();
        ErrorResponseDto responseDto = new ErrorResponseDto(ErrorCode.BAD_REQUEST.getCode(), message);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles type mismatch exceptions for request parameters.
     *
     * @param ex the MethodArgumentTypeMismatchException
     * @return an error response with 400 status
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Type mismatch for parameter: {}", ex.getName());
        String message = "Invalid value for parameter '" + ex.getName() + "': " + ex.getValue();
        ErrorResponseDto responseDto = new ErrorResponseDto(ErrorCode.BAD_REQUEST.getCode(), message);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles database constraint violations.
     *
     * @param ex the DataIntegrityViolationException
     * @return an error response with 400 status
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Database constraint violation: {}", ex.getMessage());
        String message = "Database constraint violated. Please check your input.";
        if (ex.getMessage().contains("users_username_key")) {
            message = "Username already exists.";
        }
        ErrorResponseDto responseDto = new ErrorResponseDto(ErrorCode.ALREADY_EXIST.getCode(), message);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles token blacklist exceptions from filter.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return an error response with 401 status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleBlacklistException(IllegalArgumentException ex, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Token blacklist error: {}", ex.getMessage());
        ErrorResponseDto responseDto = new ErrorResponseDto(ErrorCode.UNAUTHORIZED.getCode(), ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles all uncaught exceptions.
     *
     * @param ex the Exception
     * @return an error response with 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponseDto responseDto = new ErrorResponseDto(ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                "An unexpected error occurred.");
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
package com.backend.dtos;

/**
 * ErrorResponse represents the structure of error messages returned by the GlobalExceptionHandler.
 */
public record ErrorResponse(String error, String message) {
}
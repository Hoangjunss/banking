package com.banking.TransactionService.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * Unified API response wrapper.
 *
 * All REST APIs MUST return this structure
 * to keep response format consistent across services.
 */
@Data
@Builder
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private Instant timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message("SUCCESS")
                .timestamp(Instant.now())
                .build();
    }

    public static ApiResponse<Void> success() {
        return ApiResponse.<Void>builder()
                .success(true)
                .message("SUCCESS")
                .timestamp(Instant.now())
                .build();
    }
}
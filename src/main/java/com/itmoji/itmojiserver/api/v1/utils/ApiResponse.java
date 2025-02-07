package com.itmoji.itmojiserver.api.v1.utils;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(HttpStatus status, String message, T data) {
    public static <T> ApiResponse<T> from(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }
}

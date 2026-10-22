package com.murilo.backend.dto.common;

public record ApiResponse<T>(
        String message,
        T data
) {
}

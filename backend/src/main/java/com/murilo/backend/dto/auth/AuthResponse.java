package com.murilo.backend.dto.auth;

public record AuthResponse (

        String token,
        UserResponse user

) {
}

package com.murilo.backend.controller;

import com.murilo.backend.domain.model.User;
import com.murilo.backend.dto.auth.AuthResponse;
import com.murilo.backend.dto.auth.LoginRequest;
import com.murilo.backend.dto.auth.RegisterRequest;
import com.murilo.backend.dto.auth.UserResponse;
import com.murilo.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping ("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping ("/login")
    public AuthResponse login(
            @RequestBody @Valid LoginRequest request
    ) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return UserResponse.from(user);
    }
}

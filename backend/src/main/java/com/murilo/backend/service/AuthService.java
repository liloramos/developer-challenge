package com.murilo.backend.service;

import com.murilo.backend.domain.enums.Role;
import com.murilo.backend.domain.model.User;
import com.murilo.backend.dto.auth.AuthResponse;
import com.murilo.backend.dto.auth.LoginRequest;
import com.murilo.backend.dto.auth.RegisterRequest;
import com.murilo.backend.dto.auth.UserResponse;
import com.murilo.backend.repository.UserRepository;
import com.murilo.backend.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {

        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email já cadastrado."
            );
        }

        if (request.role() == Role.APROVADOR
                && request.approverLevel() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Aprovadores devem possuir um nível de aprovação."
            );
        }
        
        if (request.role() != Role.APROVADOR
                && request.approverLevel() != null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Somente aprovadores podem possuir nível de aprovação."
            );
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPasswordHash(
                passwordEncoder.encode(request.password())
        );
        user.setRole(request.role());
        user.setApproverLevel(request.approverLevel());

        userRepository.save(user);

        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> 
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Email ou senha inválidos."
                        )
                );

        if (!passwordEncoder.matches(
                request.password(), 
                user.getPasswordHash()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Email ou senha inválidos."
            );
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token, 
                UserResponse.from(user)
        );
    }
}

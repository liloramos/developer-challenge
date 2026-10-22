package com.murilo.backend.dto.auth;

import com.murilo.backend.domain.enums.ApproverLevel;
import com.murilo.backend.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest (

        @NotBlank(message = "Nome é obrigatório.")
        String name,

        @NotBlank(message = "Email é obrigatório.")
        @Email(message = "Email inválido.")
        String email,

        @NotBlank(message = "Senha é obrigatória.")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
        String password,

        @NotNull(message = "Papel é obrigatório.")
        Role role,
        
        ApproverLevel approverLevel

) {
}

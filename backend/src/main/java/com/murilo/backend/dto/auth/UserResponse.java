package com.murilo.backend.dto.auth;

import com.murilo.backend.domain.enums.ApproverLevel;
import com.murilo.backend.domain.enums.Role;
import com.murilo.backend.domain.model.User;

public record UserResponse (

        Long id,
        String name,
        String email,
        Role role,
        ApproverLevel approverLevel

) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getApproverLevel()
        );
    }
}

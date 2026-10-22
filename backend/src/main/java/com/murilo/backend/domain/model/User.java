package com.murilo.backend.domain.model;

import com.murilo.backend.domain.enums.ApproverLevel; 
import com.murilo.backend.domain.enums.Role;
import jakarta.persistence.*;

@Entity 
@Table (
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email")
    }
)

public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 180)
    private String email;

    @Column (nullable = false)
    private String passwordHash;

    @Enumerated (EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @Enumerated (EnumType.STRING)
    @Column(length = 30)
    private ApproverLevel approverLevel;

    public User() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public ApproverLevel getApproverLevel() {
        return approverLevel;
    }
    public void setApproverLevel(ApproverLevel approverLevel) {
        this.approverLevel = approverLevel;
    }
}

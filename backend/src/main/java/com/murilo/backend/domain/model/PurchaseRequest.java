package com.murilo.backend.domain.model;

import com.murilo.backend.domain.enums.ApprovalLevel;
import com.murilo.backend.domain.enums.RequestCategory;
import com.murilo.backend.domain.enums.RequestStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "purchase_requests")
public class PurchaseRequest {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RequestCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RequestStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApprovalLevel requiredApprovalLevel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Version 
    private Long version;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public PurchaseRequest() {
    }

    @PrePersist 
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public RequestCategory getCategory() {
        return category;
    }
    public void setCategory(RequestCategory category) {
        this.category = category;
    }

    public RequestStatus getStatus() {
        return status;
    }
    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public ApprovalLevel getRequiredApprovalLevel() {
        return requiredApprovalLevel;
    }
    public void setRequiredApprovalLevel(ApprovalLevel requiredApprovalLevel) {
        this.requiredApprovalLevel = requiredApprovalLevel;
    }

    public User getRequester() {
        return requester;
    }
    public void setRequester(User requester) {
        this.requester = requester;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

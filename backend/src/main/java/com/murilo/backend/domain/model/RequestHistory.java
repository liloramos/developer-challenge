package com.murilo.backend.domain.model;

import com.murilo.backend.domain.enums.HistoryAction;
import jakarta.persistence.*;

import java.time.Instant;

@Entity 
@Table (name = "request_history")
public class RequestHistory {

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "request_id", nullable = false)
    private PurchaseRequest request;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "user_id", nullable = false)
    private User actor;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false, length = 30)
    private HistoryAction action;

    @Column (length = 1000)
    private String comment;

    @Column (nullable = false, updatable = false)
    private Instant createdAt;

    public RequestHistory() {
    }

    @PrePersist 
    public void prePersist() {
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public PurchaseRequest getRequest() {
        return request;
    }
    public void setRequest(PurchaseRequest request) {
        this.request = request;
    }

    public User getActor() {
        return actor;
    }
    public void setActor(User actor) {
        this.actor = actor;
    }

    public HistoryAction getAction() {
        return action;
    }
    public void setAction(HistoryAction action) {
        this.action = action;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

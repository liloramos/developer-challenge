package com.murilo.backend.dto.request;

import com.murilo.backend.domain.enums.ApprovalLevel;
import com.murilo.backend.domain.enums.RequestCategory;
import com.murilo.backend.domain.enums.RequestStatus;
import com.murilo.backend.domain.model.PurchaseRequest;

import java.math.BigDecimal;
import java.time.Instant;

public record PurchaseRequestResponse(
        Long id,
        String title,
        String description,
        BigDecimal amount,
        RequestCategory category,
        RequestStatus status,
        ApprovalLevel requiredApprovalLevel,
        Long requesterId,
        String requesterName,
        Instant createdAt,
        Instant updatedAt
) {

    public static PurchaseRequestResponse from(PurchaseRequest request) {
        return new PurchaseRequestResponse(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getAmount(),
                request.getCategory(),
                request.getStatus(),
                request.getRequiredApprovalLevel(),
                request.getRequester().getId(),
                request.getRequester().getName(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }
}

package com.murilo.backend.dto.request;

import com.murilo.backend.domain.enums.HistoryAction;
import com.murilo.backend.domain.model.RequestHistory;

import java.time.Instant;

public record RequestHistoryResponse(
        Long id,
        HistoryAction action,
        String comment,
        Long actorId,
        String actorName,
        Instant createdAt
) {

    public static RequestHistoryResponse from(RequestHistory history) {
        return new RequestHistoryResponse(
                history.getId(),
                history.getAction(),
                history.getComment(),
                history.getActor().getId(),
                history.getActor().getName(),
                history.getCreatedAt()
        );
    }
}

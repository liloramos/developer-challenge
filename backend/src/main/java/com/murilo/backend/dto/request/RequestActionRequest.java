package com.murilo.backend.dto.request;

import jakarta.validation.constraints.Size;

public record RequestActionRequest(

        @Size(
                max = 1000,
                message = "Comment must have at most 1000 characters."
        )
        String comment
) {
}

package com.murilo.backend.dto.request;

import com.murilo.backend.domain.enums.RequestCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreatePurchaseRequestRequest(

        @NotBlank(message = "Title is required.")
        @Size(max = 200, message = "Title must have at most 200 characters.")
        String title,

        @NotBlank(message = "Description is required.")
        @Size(max = 2000, message = "Description must have at most 2000 characters.")
        String description,

        @NotNull(message = "Amount is required.")
        @DecimalMin(
                value = "0.01",
                message = "Amount must be greater than zero."
        )
        BigDecimal amount,

        @NotNull(message = "Category is required.")
        RequestCategory category
) {
}

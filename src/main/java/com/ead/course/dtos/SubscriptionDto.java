package com.ead.course.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubscriptionDto(
        @NotNull(message = "UserId is required") UUID userId
) {
}

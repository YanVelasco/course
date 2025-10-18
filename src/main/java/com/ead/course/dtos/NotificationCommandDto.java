package com.ead.course.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record NotificationCommandDto(
        String title,
        String message,
        UUID userId
) {
}

package com.ead.course.dtos;

import java.util.UUID;

public record CourseUserDto(UUID userId, UUID courseId) {
}

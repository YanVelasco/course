package com.ead.course.dtos;

import jakarta.validation.constraints.NotBlank;

public record LessonDto(

        @NotBlank(message = "Lesson title cannot be empty")
        String title,

        @NotBlank(message = "Lesson description cannot be empty")
        String description,

        @NotBlank(message = "Lesson video URL cannot be empty")
        String videoUrl

) {
}

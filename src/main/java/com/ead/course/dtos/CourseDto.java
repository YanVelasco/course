package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseDto(

        @NotBlank(message = "Course name cannot be empty")
        String name,

        @NotBlank(message = "Course description cannot be empty")
        String description,

        @NotNull(message = "Course status cannot be null")
        CourseStatus courseStatus,

        @NotNull(message = "Course level cannot be null")
        CourseLevel courseLevel,

        @NotNull(message = "User instructor cannot be null")
        UUID userInstructor,

        String imageUrl

) {
}

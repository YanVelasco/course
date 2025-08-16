package com.ead.course.dtos;

import jakarta.validation.constraints.NotBlank;

public record ModuleDTO(

        @NotBlank(message = "Module title cannot be empty")
        String title,

        @NotBlank(message = "Module description cannot be empty")
        String description

) {
}

package com.ead.course.exceptions.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDto(
        int statusCode,
        String message,
        Map<String,String> errorDetails
) {
}

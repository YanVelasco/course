package com.ead.course.exceptions.globalexception;

import com.ead.course.exceptions.NotFoundException;
import com.ead.course.exceptions.response.ErrorResponseDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        var errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex) {
        var errorResponse = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        Throwable root = ex.getMostSpecificCause();

        if (root instanceof InvalidFormatException ife) {
            String fieldName = (ife.getPath() != null && !ife.getPath().isEmpty())
                    ? ife.getPath().get(ife.getPath().size() - 1).getFieldName()
                    : "value";
            Class<?> targetType = ife.getTargetType();
            if (targetType != null && targetType.isEnum()) {
                String accepted = Arrays.stream(targetType.getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                errors.put(fieldName, "Enum value incorrect '" + ife.getValue() + "'. Accept values: " + accepted);
                return ResponseEntity.badRequest().body(new ErrorResponseDto(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid Enum value",
                        errors
                ));
            }
            errors.put(fieldName, "Invalid value: " + ife.getValue());
        } else if (root instanceof JsonParseException jpe) {
            errors.put("json", "Malformed JSON: " + jpe.getOriginalMessage());
        } else {
            errors.put("message", root.getMessage());
        }

        return ResponseEntity.badRequest().body(new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON request",
                errors
        ));

    }

}

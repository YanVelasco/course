package com.ead.course.dtos;

import com.ead.course.models.LessonModel;
import org.springframework.data.domain.Page;

import java.util.List;

public record LessonPageDto(
        List<LessonModel> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last,
        boolean first,
        boolean empty
) {
    public static LessonPageDto from(Page<LessonModel> page) {
        return new LessonPageDto(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst(),
                page.isEmpty()
        );
    }
}

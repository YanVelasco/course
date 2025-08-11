package com.ead.course.dtos;

import com.ead.course.models.CourseModel;
import org.springframework.data.domain.Page;

import java.util.List;

public record CoursePageDto(
        List<CourseModel> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last,
        boolean first,
        boolean empty
) {
    public static CoursePageDto from(Page<CourseModel> page) {
        return new CoursePageDto(
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

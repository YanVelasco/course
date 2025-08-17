package com.ead.course.dtos;

import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Page;

import java.util.List;

public record ModulePageDto(
        CourseModel course,
        List<ModuleModel> modules,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last,
        boolean first,
        boolean empty
) {

    public static ModulePageDto from(Page<ModuleModel> page, CourseModel course) {
        return new ModulePageDto(
                course,
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

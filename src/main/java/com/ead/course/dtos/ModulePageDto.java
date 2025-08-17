package com.ead.course.dtos;

import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Page;

import java.util.List;

public record ModulePageDto(
        List<ModuleModel> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last,
        boolean first,
        boolean empty
) {

    public static ModulePageDto from(Page<ModuleModel> page) {
        return new ModulePageDto(
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

package com.ead.course.dtos;

import com.ead.course.models.UserModel;
import org.springframework.data.domain.Page;

import java.util.List;

public record UserPageDto(
        List<UserModel> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last,
        boolean first,
        boolean empty
) {

    public static UserPageDto from(Page<UserModel> page) {
        return new UserPageDto(
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

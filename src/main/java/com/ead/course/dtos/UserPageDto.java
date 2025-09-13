package com.ead.course.dtos;

import java.util.List;

public record UserPageDto(
        List<UserDto> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last,
        boolean first,
        boolean empty
) {
}

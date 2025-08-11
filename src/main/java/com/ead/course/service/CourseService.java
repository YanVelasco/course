package com.ead.course.service;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import jakarta.validation.constraints.NotBlank;

public interface CourseService {

    void delete(CourseModel courseModel);

    CourseModel save(CourseDto courseDto);

    Boolean existsByName(@NotBlank(message = "Course name cannot be empty") String name);

}

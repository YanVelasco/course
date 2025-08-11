package com.ead.course.service;

import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.CoursePageDto;
import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.ead.course.models.CourseModel;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CourseService {

    void delete(CourseModel courseModel);

    CourseModel save(CourseDto courseDto);

    Boolean existsByName(@NotBlank(message = "Course name cannot be empty") String name);

    CoursePageDto findAll(Pageable pageable, String name, CourseStatus courseStatus, String description,
                          CourseLevel courseLevel, UUID userInstructor);

    CourseModel findCourseById(UUID courseId);

}

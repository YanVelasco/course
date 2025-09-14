package com.ead.course.service;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface CourseUserService {
    void existsByCourseAndUserId(CourseModel course, UUID uuid);

    CourseUserModel saveAndSandSubscriptionUserInCourse(CourseUserModel courseUserModel);
}

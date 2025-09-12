package com.ead.course.service.impls;

import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.service.CourseUserService;

public class CourseUserServiceImpl implements CourseUserService {

    final CourseUserRepository courseUserRepository;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }
}

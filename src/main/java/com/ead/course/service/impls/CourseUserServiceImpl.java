package com.ead.course.service.impls;

import com.ead.course.exceptions.AlreadySubscribedException;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.service.CourseUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    final CourseUserRepository courseUserRepository;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }

    @Transactional
    @Override
    public void existsByCourseAndUserId(CourseModel course, UUID uuid) {
        if (courseUserRepository.existsByCourseAndUserId(course, uuid)) {
            throw new AlreadySubscribedException("User already subscribed to this course.");
        }
    }

    @Transactional
    @Override
    public CourseUserModel saveAndSandSubscriptionUserInCourse(CourseUserModel courseUserModel) {
        courseUserModel = courseUserRepository.save(courseUserModel);

        return courseUserModel;
    }

}

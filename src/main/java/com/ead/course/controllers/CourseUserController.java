package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserPageDto;
import com.ead.course.models.CourseUserModel;
import com.ead.course.service.CourseService;
import com.ead.course.service.CourseUserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CourseUserController {

    final AuthUserClient authUserClient;
    final CourseService courseService;
    final CourseUserService courseUserService;

    public CourseUserController(AuthUserClient authUserClient, CourseService courseService,
                                CourseUserService courseUserService) {
        this.authUserClient = authUserClient;
        this.courseUserService = courseUserService;
        this.courseService = courseService;
    }

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<UserPageDto> getAlUsersByCourse(
            Pageable pageable,
            @PathVariable(value = "courseId") UUID courseId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                authUserClient.getAllUsersByCourse(courseId, pageable)
        );
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid SubscriptionDto subscriptionDto
    ) {
        var course = courseService.findCourseById(courseId);
        courseUserService.existsByCourseAndUserId(course, subscriptionDto.userId());

        CourseUserModel courseUserModel = courseUserService.saveAndSandSubscriptionUserInCourse(
                course.convertToCourseUserModel(
                        subscriptionDto.userId()
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }

}

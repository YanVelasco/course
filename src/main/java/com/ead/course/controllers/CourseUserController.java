package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserPageDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.service.CourseService;
import com.ead.course.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CourseUserController {

    final CourseService courseService;
    final UserService userService;

    public CourseUserController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<UserPageDto> getAlUsersByCourse(
            Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String userStatus,
            @RequestParam(required = false) String UserType,
            @PathVariable(value = "courseId") UUID courseId
    ) {
        courseService.findCourseById(courseId);

        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserByCourse(
                courseId, pageable, name, fullName, userStatus, UserType
        ));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid SubscriptionDto subscriptionDto
    ) {
        var course = courseService.findCourseById(courseId);
        var user = userService.findById(subscriptionDto.userId());
        if (courseService.existsByCourseAndUser(course, user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: User already enrolled in this course.");
        }
        if (user.getUserStatus().equals(UserStatus.BLOCKED.toString())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: User is blocked.");
        }
        courseService.saveSubscriptionUserInCourse(course, user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User enrolled in course successfully.");
    }

}

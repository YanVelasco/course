package com.ead.course.validations;

import com.ead.course.configs.security.UserDetailsImpl;
import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.UserType;
import com.ead.course.service.CourseService;
import com.ead.course.service.UserService;
import com.ead.course.utils.SecurityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    Logger logger = LogManager.getLogger(CourseValidator.class);

    final Validator validator;
    final CourseService courseService;
    final UserService userService;

    public CourseValidator(@Qualifier("defaultValidator") Validator validator, CourseService courseService,
                           UserService userService
    ) {
        this.validator = validator;
        this.courseService = courseService;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CourseDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseDto courseDto = (CourseDto) target;
        logger.debug("Validating course: {}", courseDto);
        validator.validate(target, errors);
        if (!errors.hasErrors()) {
            validateCourseName(courseDto, errors);
            validateUserInstructor(courseDto.userInstructor(), errors);
        }
    }

    public void validateCourseName(CourseDto courseDto, Errors errors) {
        logger.debug("Validating course: {}", courseDto);
        if (courseService.existsByName(courseDto.name())) {
            errors.rejectValue("name", "courseNameConflict", "Course with this name already exists.");
            logger.warn("Course with name {} already exists.", courseDto.name());
        }
    }

    public void validateUserInstructor(UUID userInstructor, Errors errors) {
        logger.debug("Validating user instructor: {}", userInstructor);
        UserDetailsImpl authenticatedUser = SecurityUtils.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in security context."));
        if (authenticatedUser.getUserId().equals(userInstructor) || SecurityUtils.isAdmin()) {
            var userModel = userService.findById(userInstructor);
            if (userModel.getUserType().equals(UserType.STUDENT.toString()) || userModel.getUserType().equals(UserType.USER.toString())) {
                errors.rejectValue("userInstructor", "userInstructorInvalid", "User instructor must be an INSTRUCTOR " +
                        "or " +
                        "ADMIN.");
                logger.warn("User instructor with ID {} is not valid. Must be INSTRUCTOR or ADMIN.", userInstructor);
            }
        } else {
            errors.rejectValue("userInstructor", "userInstructorUnauthorized", "You are not authorized to assign this" +
                    " user as instructor.");
            logger.warn("Authenticated user with ID {} is not authorized to assign user with ID {} as instructor.",
                    authenticatedUser.getUserId(), userInstructor);
            throw new AccessDeniedException("FORBIDDEN: You are not authorized to assign this user as instructor.");
        }

    }

}

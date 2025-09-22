package com.ead.course.validations;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.enums.UserType;
import com.ead.course.service.CourseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    Logger logger = LogManager.getLogger(CourseValidator.class);

    final Validator validator;
    final CourseService courseService;
    final AuthUserClient authUserClient;

    public CourseValidator(@Qualifier("defaultValidator") Validator validator, CourseService courseService,
                           AuthUserClient authUserClient) {
        this.validator = validator;
        this.courseService = courseService;
        this.authUserClient = authUserClient;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
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
        if (userInstructor != null) {
            ResponseEntity<UserDto> responseUserInstructor = authUserClient.getOneUserByUserId(userInstructor);
            assert responseUserInstructor.getBody() != null;
            if (responseUserInstructor.getBody().userType().equals(UserType.STUDENT) || responseUserInstructor.getBody().userType().equals(UserType.USER)) {
                errors.rejectValue("userInstructor", "userInstructorError", "User must be an instructor.");
                logger.error("Error validation instructor: {}", userInstructor);
            }
        }
    }
}

package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.ead.course.service.CourseService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private static final Logger logger = LogManager.getLogger(CourseController.class);
    final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<Object> saveCourse(
            @RequestBody @Valid CourseDto courseDto
    ){
        logger.debug("POST saveCourse: {}", courseDto);
        if (courseService.existsByName(courseDto.name())) {
            logger.warn("Course with name {} already exists.", courseDto.name());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Course with name " + courseDto.name() + " already exists.");
        }
        var saved = courseService.save(courseDto);
        logger.debug("Course created: {}", saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<Object> getAllCourses(
            Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) CourseStatus courseStatus,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) CourseLevel courseLevel,
            @RequestParam(required = false) UUID userInstructor
    ) {
        logger.debug("GET getAllCourses: name={}, courseStatus={}, description={}, courseLevel={}, userInstructor={}",
                name, courseStatus, description, courseLevel, userInstructor);
        var result = courseService.findAll(
                pageable, name, courseStatus, description, courseLevel, userInstructor);
        logger.debug("Courses found: {}", result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getCourseById(
            @PathVariable UUID courseId
    ) {
        logger.debug("GET getCourseById: courseId={}", courseId);
        var course = courseService.findCourseById(courseId);
        logger.debug("Course found: {}", course);
        return ResponseEntity.status(HttpStatus.OK).body(course);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(
            @PathVariable UUID courseId,
            @RequestBody @Valid CourseDto courseDto
    ) {
        logger.debug("PUT updateCourse: courseId={}, courseDto={}", courseId, courseDto);
        if (courseService.existsByName(courseDto.name())) {
            logger.warn("Course with name {} already exists.", courseDto.name());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Course with name " + courseDto.name() + " already exists.");
        }
        var courseModel = courseService.findCourseById(courseId);
        var updated = courseService.updateCourse(courseModel, courseDto);
        logger.debug("Course updated: {}", updated);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable UUID courseId
    ) {
        logger.debug("DELETE deleteCourse: courseId={}", courseId);
        courseService.delete(courseService.findCourseById(courseId));
        logger.debug("Course deleted: {}", courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

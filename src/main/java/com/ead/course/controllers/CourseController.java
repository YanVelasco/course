package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.ead.course.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {

    final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<Object> saveCourse(
            @RequestBody @Valid CourseDto courseDto
    ){
        if (courseService.existsByName(courseDto.name())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Course with name " + courseDto.name() + " already exists.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseDto));
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
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(
                pageable, name, courseStatus, description, courseLevel, userInstructor));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getCourseById(
            @PathVariable UUID courseId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findCourseById(courseId));
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(
            @PathVariable UUID courseId,
            @RequestBody @Valid CourseDto courseDto
    ) {
        if (courseService.existsByName(courseDto.name())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Course with name " + courseDto.name() + " already exists.");
        }
        var courseModel = courseService.findCourseById(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(courseService.updateCourse(courseModel, courseDto));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable UUID courseId
    ) {
        courseService.delete(courseService.findCourseById(courseId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

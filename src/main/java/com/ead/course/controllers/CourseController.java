package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

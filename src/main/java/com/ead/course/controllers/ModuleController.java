package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDTO;
import com.ead.course.models.ModuleModel;
import com.ead.course.service.CourseService;
import com.ead.course.service.ModuleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class ModuleController {

    final ModuleService moduleService;
    private final CourseService courseService;

    public ModuleController(ModuleService moduleService, CourseService courseService) {
        this.moduleService = moduleService;
        this.courseService = courseService;
    }

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(
            @PathVariable("courseId") UUID courseId,
            @RequestBody @Valid ModuleDTO moduleDTO
    ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.saveModule(courseService.findCourseById(courseId), moduleDTO));
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> getAllModules(
            Pageable pageable,
            @PathVariable("courseId") UUID courseId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllModulesIntoCourse(
                pageable, courseService.findCourseById(courseId), title, description));
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(
            @PathVariable("courseId") UUID courseId,
            @PathVariable("moduleId") UUID moduleId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findModuleIntoCourse(courseService.findCourseById(courseId), moduleId));
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(
            @PathVariable("courseId") UUID courseId,
            @PathVariable("moduleId") UUID moduleId
    ) {
        ModuleModel module = moduleService.findModuleIntoCourse(courseService.findCourseById(courseId), moduleId);
        moduleService.deleteModule(module);
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully");
    }


    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(
            @PathVariable("courseId") UUID courseId,
            @PathVariable("moduleId") UUID moduleId,
            @RequestBody @Valid ModuleDTO moduleDTO
    ) {
        ModuleModel module = moduleService.findModuleIntoCourse(courseService.findCourseById(courseId), moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.updateModule(module, moduleDTO));
    }

}

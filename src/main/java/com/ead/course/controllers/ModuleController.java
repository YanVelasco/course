package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDTO;
import com.ead.course.models.ModuleModel;
import com.ead.course.service.CourseService;
import com.ead.course.service.ModuleService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class ModuleController {

    private static final Logger logger = LogManager.getLogger(ModuleController.class);
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
        logger.debug("POST saveModule: courseId={}, moduleDTO={}", courseId, moduleDTO);
        var saved = moduleService.saveModule(courseService.findCourseById(courseId), moduleDTO);
        logger.debug("Module created: {}", saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> getAllModules(
            Pageable pageable,
            @PathVariable("courseId") UUID courseId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description
    ) {
        logger.debug("GET getAllModules: courseId={}, title={}, description={}", courseId, title, description);
        var result = moduleService.findAllModulesIntoCourse(
                pageable, courseService.findCourseById(courseId), title, description);
        logger.debug("Modules found: {}", result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(
            @PathVariable("courseId") UUID courseId,
            @PathVariable("moduleId") UUID moduleId
    ) {
        logger.debug("GET getOneModule: courseId={}, moduleId={}", courseId, moduleId);
        var module = moduleService.findModuleIntoCourse(courseService.findCourseById(courseId), moduleId);
        logger.debug("Module found: {}", module);
        return ResponseEntity.status(HttpStatus.OK).body(module);
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(
            @PathVariable("courseId") UUID courseId,
            @PathVariable("moduleId") UUID moduleId
    ) {
        logger.debug("DELETE deleteModule: courseId={}, moduleId={}", courseId, moduleId);
        ModuleModel module = moduleService.findModuleIntoCourse(courseService.findCourseById(courseId), moduleId);
        moduleService.deleteModule(module);
        logger.debug("Module deleted: {}", moduleId);
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully");
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(
            @PathVariable("courseId") UUID courseId,
            @PathVariable("moduleId") UUID moduleId,
            @RequestBody @Valid ModuleDTO moduleDTO
    ) {
        logger.debug("PUT updateModule: courseId={}, moduleId={}, moduleDTO={}", courseId, moduleId, moduleDTO);
        ModuleModel module = moduleService.findModuleIntoCourse(courseService.findCourseById(courseId), moduleId);
        var updated = moduleService.updateModule(module, moduleDTO);
        logger.debug("Module updated: {}", updated);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

}

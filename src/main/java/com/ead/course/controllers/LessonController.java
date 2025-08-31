package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.dtos.LessonPageDto;
import com.ead.course.models.LessonModel;
import com.ead.course.service.LessonService;
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
public class LessonController {

    private static final Logger logger = LogManager.getLogger(LessonController.class);
    final LessonService lessonService;
    final ModuleService moduleService;

    public LessonController(LessonService lessonService, ModuleService moduleService) {
        this.moduleService = moduleService;
        this.lessonService = lessonService;
    }

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveModule(
            @PathVariable("moduleId") UUID moduleId,
            @RequestBody @Valid LessonDto lessonDto
    ) {
        logger.debug("POST saveLesson: moduleId={}, lessonDto={}", moduleId, lessonDto);
        var saved = lessonService.saveLesson(moduleService.findModuleById(moduleId), lessonDto);
        logger.debug("Lesson created: {}", saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<LessonPageDto> getAllLessons(
            Pageable pageable,
            @PathVariable("moduleId") UUID moduleId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description
    ) {
        logger.debug("GET getAllLessons: moduleId={}, title={}, description={}", moduleId, title, description);
        var result = lessonService.findAllLessonsIntoModule(
                pageable, moduleService.findModuleById(moduleId), title, description
        );
        logger.debug("Lessons found: {}", result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(
            @PathVariable("moduleId") UUID moduleId,
            @PathVariable("lessonId") UUID lessonId
    ) {
        logger.debug("GET getOneLesson: moduleId={}, lessonId={}", moduleId, lessonId);
        var lesson = lessonService.findOneLessonIntoModule(moduleService.findModuleById(moduleId), lessonId);
        logger.debug("Lesson found: {}", lesson);
        return ResponseEntity.status(HttpStatus.OK).body(lesson);
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(
            @PathVariable("moduleId") UUID moduleId,
            @PathVariable("lessonId") UUID lessonId
    ) {
        logger.debug("DELETE deleteLesson: moduleId={}, lessonId={}", moduleId, lessonId);
        lessonService.deleteLesson(
                lessonService.findOneLessonIntoModule(moduleService.findModuleById(moduleId), lessonId)
        );
        logger.debug("Lesson deleted: {}", lessonId);
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(
            @PathVariable("moduleId") UUID moduleId,
            @PathVariable("lessonId") UUID lessonId,
            @RequestBody @Valid LessonDto lessonDto
    ) {
        logger.debug("PUT updateLesson: moduleId={}, lessonId={}, lessonDto={}", moduleId, lessonId, lessonDto);
        LessonModel lesson = lessonService.findOneLessonIntoModule(moduleService.findModuleById(moduleId), lessonId);
        var updated = lessonService.updateLesson(lesson, lessonDto);
        logger.debug("Lesson updated: {}", updated);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

}

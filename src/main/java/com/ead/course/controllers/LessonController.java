package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.dtos.LessonPageDto;
import com.ead.course.dtos.ModuleDTO;
import com.ead.course.models.ModuleModel;
import com.ead.course.service.LessonService;
import com.ead.course.service.ModuleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class LessonController {

    final LessonService lessonService;
    final ModuleService moduleService;

    public LessonController(LessonService lessonService, ModuleService moduleService) {
        this.lessonService = lessonService;
        this.moduleService = moduleService;
    }

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveModule(
            @PathVariable("moduleId") UUID moduleId,
            @RequestBody @Valid LessonDto lessonDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                lessonService.saveLesson(moduleService.findModuleById(moduleId), lessonDto)
        );
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<LessonPageDto> getAllLessons(
            Pageable pageable,
            @PathVariable("moduleId") UUID moduleId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                lessonService.findAllLessonsIntoModule(
                        pageable, moduleService.findModuleById(moduleId), title, description
                )
        );
    }

}

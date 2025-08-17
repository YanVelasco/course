package com.ead.course.service;

import com.ead.course.dtos.LessonDto;
import com.ead.course.dtos.LessonPageDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LessonService {

    void deleteLesson(LessonModel lessonModel);

    LessonModel saveLesson(ModuleModel moduleById, LessonDto lessonDto);

    LessonPageDto findAllLessonsIntoModule(Pageable pageable, ModuleModel moduleById, String title, String description);

    LessonModel findOneLessonIntoModule(ModuleModel moduleById, UUID lessonId);


    LessonModel updateLesson(LessonModel lesson, LessonDto lessonDto);

}

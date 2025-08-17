package com.ead.course.service;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import jakarta.validation.Valid;

public interface LessonService {

    void deleteLesson(LessonModel lessonModel);

    LessonModel saveLesson(ModuleModel moduleById, LessonDto lessonDto);

}

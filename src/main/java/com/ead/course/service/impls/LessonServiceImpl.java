package com.ead.course.service.impls;

import com.ead.course.service.LessonService;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {

    final LessonService lessonService;

    public LessonServiceImpl(LessonService lessonService) {
        this.lessonService = lessonService;
    }

}

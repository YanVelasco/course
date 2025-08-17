package com.ead.course.service.impls;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.service.LessonService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class LessonServiceImpl implements LessonService {

    final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    @Override
    public void deleteLesson(LessonModel lessonModel) {
        lessonRepository.delete(lessonModel);
    }

    @Override
    public LessonModel saveLesson(ModuleModel moduleById, LessonDto lessonDto) {
        LessonModel lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setModule(moduleById);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return lessonRepository.save(lessonModel);
    }

}

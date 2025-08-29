package com.ead.course.service.impls;

import com.ead.course.dtos.LessonDto;
import com.ead.course.dtos.LessonPageDto;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.service.LessonService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Override
    public LessonPageDto findAllLessonsIntoModule(Pageable pageable, ModuleModel moduleById, String title, String description) {
        List<Specification<LessonModel>> specs = new ArrayList<>();

        specs.add((root, query, cb) -> cb.equal(root.get("module").get("moduleId"), moduleById.getModuleId()));

        if (title != null && !title.isBlank()) {
            String t = title.toLowerCase();
            specs.add((root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + t + "%"));
        }

        if (description != null && !description.isBlank()) {
            String d = description.toLowerCase();
            specs.add((root, query, cb) -> cb.like(cb.lower(root.get("description")), "%" + d + "%"));
        }

        Specification<LessonModel> spec = Specification.allOf(specs);
        Page<LessonModel> pageResult = lessonRepository.findAll(spec, pageable);
        return LessonPageDto.from(pageResult);
    }

    @Override
    public LessonModel findOneLessonIntoModule(ModuleModel moduleById, UUID lessonId) {
        return lessonRepository.findByLessonIdAndModule(lessonId, moduleById)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Lesson with id %s not found in module %s", lessonId, moduleById.getModuleId())
                ));
    }

    @Override
    public LessonModel updateLesson(LessonModel lesson, LessonDto lessonDto) {
        BeanUtils.copyProperties(lessonDto, lesson);
        return lessonRepository.save(lesson);
    }


}

package com.ead.course.service.impls;

import com.ead.course.dtos.LessonDto;
import com.ead.course.dtos.LessonPageDto;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.service.LessonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(LessonServiceImpl.class);
    final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    @Override
    public void deleteLesson(LessonModel lessonModel) {
        logger.debug("Deleting lesson: {}", lessonModel);
        lessonRepository.delete(lessonModel);
        logger.debug("Lesson deleted: {}", lessonModel.getLessonId());
    }

    @Override
    public LessonModel saveLesson(ModuleModel moduleById, LessonDto lessonDto) {
        logger.debug("Saving new lesson for module {}: {}", moduleById.getModuleId(), lessonDto);
        LessonModel lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setModule(moduleById);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        var saved = lessonRepository.save(lessonModel);
        logger.debug("Lesson saved: {}", saved);
        return saved;
    }

    @Override
    public LessonPageDto findAllLessonsIntoModule(Pageable pageable, ModuleModel moduleById, String title, String description) {
        logger.debug("Finding all lessons in module {} with filters - title: {}, description: {}", moduleById.getModuleId(), title, description);
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
        logger.debug("Lessons page result: {}", pageResult);
        return LessonPageDto.from(pageResult);
    }

    @Override
    public LessonModel findOneLessonIntoModule(ModuleModel moduleById, UUID lessonId) {
        logger.debug("Finding lesson {} in module {}", lessonId, moduleById.getModuleId());
        return lessonRepository.findByLessonIdAndModule(lessonId, moduleById)
                .orElseThrow(() -> {
                    logger.error("Lesson with id {} not found in module {}", lessonId, moduleById.getModuleId());
                    return new NotFoundException(
                        String.format("Lesson with id %s not found in module %s", lessonId, moduleById.getModuleId())
                    );
                });
    }

    @Override
    public LessonModel updateLesson(LessonModel lesson, LessonDto lessonDto) {
        logger.debug("Updating lesson: {}, with data: {}", lesson, lessonDto);
        BeanUtils.copyProperties(lessonDto, lesson);
        var updated = lessonRepository.save(lesson);
        logger.debug("Lesson updated: {}", updated);
        return updated;
    }


}

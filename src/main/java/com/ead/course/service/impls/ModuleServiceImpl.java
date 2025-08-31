package com.ead.course.service.impls;

import com.ead.course.dtos.ModuleDTO;
import com.ead.course.dtos.ModulePageDto;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.service.ModuleService;
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
public class ModuleServiceImpl implements ModuleService {

    private static final Logger logger = LogManager.getLogger(ModuleServiceImpl.class);
    final ModuleRepository moduleRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Transactional
    @Override
    public void deleteModule(ModuleModel module) {
        logger.debug("Deleting module: {}", module);
        moduleRepository.delete(module);
        logger.debug("Module deleted: {}", module.getModuleId());
    }

    @Transactional
    @Override
    public ModuleModel saveModule(CourseModel courseById, ModuleDTO moduleDTO) {
        logger.debug("Saving new module for course {}: {}", courseById.getCourseId(), moduleDTO);
        ModuleModel moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDTO, moduleModel);
        moduleModel.setCourse(courseById);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        var saved = moduleRepository.save(moduleModel);
        logger.debug("Module saved: {}", saved);
        return saved;
    }

    @Transactional
    @Override
    public ModulePageDto findAllModulesIntoCourse(Pageable pageable, CourseModel courseById, String title,
                                                  String description) {
        logger.debug("Finding all modules in course {} with filters - title: {}, description: {}", courseById.getCourseId(), title, description);
        List<Specification<ModuleModel>> parts = new ArrayList<>();

        parts.add((root, query, cb) -> cb.equal(root.get("course").get("courseId"), courseById.getCourseId()));

        if (title != null && !title.isBlank()) {
            String t = title.toLowerCase();
            parts.add((root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + t + "%"));
        }

        if (description != null && !description.isBlank()) {
            String d = description.toLowerCase();
            parts.add((root, query, cb) -> cb.like(cb.lower(root.get("description")), "%" + d + "%"));
        }

        Specification<ModuleModel> spec = Specification.allOf(parts);
        Page<ModuleModel> pageResult = moduleRepository.findAll(spec, pageable);
        logger.debug("Modules page result: {}", pageResult);
        return ModulePageDto.from(pageResult);
    }

    @Transactional
    @Override
    public ModuleModel findModuleIntoCourse(CourseModel courseById, UUID moduleId) {
        logger.debug("Finding module {} in course {}", moduleId, courseById.getCourseId());
        return moduleRepository.findByModuleIdAndCourse(moduleId, courseById).orElseThrow(
                () -> {
                    logger.error("Module not found for this course. moduleId={}, courseId={}", moduleId, courseById.getCourseId());
                    return new RuntimeException("Module not found for this course.");
                }
        );
    }

    @Transactional
    @Override
    public ModuleModel updateModule(ModuleModel module, ModuleDTO moduleDTO) {
        logger.debug("Updating module: {}, with data: {}", module, moduleDTO);
        BeanUtils.copyProperties(moduleDTO, module);
        var updated = moduleRepository.save(module);
        logger.debug("Module updated: {}", updated);
        return updated;
    }

    @Override
    public ModuleModel findModuleById(UUID moduleId) {
        logger.debug("Finding module by id: {}", moduleId);
        return moduleRepository.findById(moduleId).orElseThrow(
                () -> new NotFoundException(
                        String.format("Module with id %s not found.", moduleId)
                )
        );
    }

}

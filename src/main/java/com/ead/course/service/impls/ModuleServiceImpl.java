package com.ead.course.service.impls;

import com.ead.course.dtos.ModuleDTO;
import com.ead.course.dtos.ModulePageDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.service.ModuleService;
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

    final ModuleRepository moduleRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Transactional
    @Override
    public void deleteModule(ModuleModel module) {
        moduleRepository.delete(module);
    }

    @Transactional
    @Override
    public ModuleModel saveModule(CourseModel courseById, ModuleDTO moduleDTO) {
        ModuleModel moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDTO, moduleModel);
        moduleModel.setCourse(courseById);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return moduleRepository.save(moduleModel);
    }

    @Transactional
    @Override
    public ModulePageDto findAllModulesIntoCourse(Pageable pageable, CourseModel courseById, String title,
                                                  String description) {
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
        return ModulePageDto.from(pageResult);
    }

    @Transactional
    @Override
    public ModuleModel findModuleIntoCourse(CourseModel courseById, UUID moduleId) {
        return moduleRepository.findByModuleIdAndCourse(moduleId, courseById).orElseThrow(
                () -> new RuntimeException("Module not found for this course.")
        );
    }

    @Transactional
    @Override
    public ModuleModel updateModule(ModuleModel module, ModuleDTO moduleDTO) {
        BeanUtils.copyProperties(moduleDTO, module);
        return moduleRepository.save(module);
    }

}

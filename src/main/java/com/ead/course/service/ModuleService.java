package com.ead.course.service;

import com.ead.course.dtos.ModuleDTO;
import com.ead.course.dtos.ModulePageDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Pageable;

public interface ModuleService {

    void deleteModule(ModuleModel module);

    ModuleModel saveModule(CourseModel courseById, ModuleDTO moduleDTO);

    ModulePageDto findAllModulesIntoCourse(Pageable pageable, CourseModel courseById, String title, String description);

}

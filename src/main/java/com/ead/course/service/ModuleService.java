package com.ead.course.service;

import com.ead.course.dtos.ModuleDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import jakarta.validation.Valid;

public interface ModuleService {

    void deleteModule(ModuleModel module);

    ModuleModel saveModule(CourseModel courseById, ModuleDTO moduleDTO);

}

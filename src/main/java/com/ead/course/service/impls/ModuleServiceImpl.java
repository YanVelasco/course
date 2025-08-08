package com.ead.course.service.impls;

import com.ead.course.repositories.ModuleRepository;
import com.ead.course.service.ModuleService;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImpl implements ModuleService {

    final ModuleRepository moduleRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

}

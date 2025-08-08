package com.ead.course.service.impls;

import com.ead.course.service.ModuleService;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImpl implements ModuleService {

    final ModuleService moduleService;

    public ModuleServiceImpl(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

}

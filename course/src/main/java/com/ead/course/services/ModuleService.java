package com.ead.course.services;

import com.ead.course.models.ModuleModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {

    public void delete(ModuleModel moduleModel);

    ModuleModel save(ModuleModel model);

    Optional<ModuleModel> findById(UUID moduleId);

    Optional<ModuleModel> findByModuleIdAndCourse(UUID moduleId, UUID courseId);

    List<ModuleModel> findAllModulesByCourse(UUID courseId);
}

package com.ead.course.services;

import com.ead.course.models.LessonModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {
    void save(LessonModel model);

    Optional<LessonModel> findByLessonIdAndModule(UUID lessonId, UUID moduleId);

    void delete(LessonModel lessonModel);

    List<LessonModel> findAllLessonsByModule(UUID moduleId);
}

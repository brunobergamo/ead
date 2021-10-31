package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {
    List<LessonModel> findByModule_ModuleId(UUID moduleId);

    Optional<LessonModel> findByLessonIdAndModule_ModuleId(UUID lessonId, UUID moduleId);


}

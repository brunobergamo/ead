package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    LessonService lessonService;

    @Autowired
    ModuleService moduleService;

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                              @RequestBody @Valid LessonDto lessonDto){
        Optional<ModuleModel> moduleModelOptional =moduleService.findById(moduleId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }

        LessonModel model = new LessonModel();
        BeanUtils.copyProperties(lessonDto,model);
        LocalDateTime utc = LocalDateTime.now(ZoneId.of("UTC"));

        model.setCreationDate(utc);
        model.setModule(moduleModelOptional.get());

        lessonService.save(model);

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId")UUID moduleId,
                                               @PathVariable(value = "lessonId")UUID lessonId){

        Optional<LessonModel> lessonModelOptional = lessonService.findByLessonIdAndModule(lessonId,moduleId);
        if(lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found");
        }
        lessonService.delete(lessonModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId")UUID moduleId,
                                               @PathVariable(value = "lessonId")UUID lessonId,
                                               @RequestBody @Valid LessonDto lessonDto){

        Optional<LessonModel> lessonModelOptional = lessonService.findByLessonIdAndModule(lessonId,moduleId);
        if(lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found");
        }

        var moduleModel = lessonModelOptional.get();
        moduleModel.setDescription(lessonDto.getDescription());
        moduleModel.setTitle(lessonDto.getTitle());
        moduleModel.setVideoUrl(lessonDto.getVideoUrl());
        lessonService.save(moduleModel);

        return ResponseEntity.ok().body(moduleModel);
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<List<LessonModel>> getAllLessons(@PathVariable(value = "moduleId")UUID moduleId){
        List<LessonModel> all = lessonService.findAllLessonsByModule(moduleId);
        return ResponseEntity.ok().body(all);
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getLesson(@PathVariable(value = "moduleId")UUID moduleId,
                                            @PathVariable(value = "lessonId")UUID lessonId){
        Optional<LessonModel> lessonModelOptional = lessonService.findByLessonIdAndModule(lessonId,moduleId);
        if(lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found");
        }
        return ResponseEntity.ok().body(lessonModelOptional.get());
    }
}

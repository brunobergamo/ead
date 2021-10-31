package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import org.apache.catalina.connector.Response;
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
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    CourseService courseService;

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModules(@PathVariable(value = "courseId") UUID courseId,
                                              @RequestBody  @Valid ModuleDto moduleDto){
        Optional<CourseModel> courseModelOptional =courseService.findById(courseId);
        if(courseModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        ModuleModel model = new ModuleModel();
        BeanUtils.copyProperties(moduleDto,model);
        LocalDateTime utc = LocalDateTime.now(ZoneId.of("UTC"));

        model.setCreationDate(utc);
        model.setCourse(courseModelOptional.get());

        moduleService.save(model);

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId")UUID courseId,
                                               @PathVariable(value = "moduleId")UUID moduleId){

        Optional<ModuleModel> moduleModelOptional = moduleService.findByModuleIdAndCourse(moduleId,courseId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }
        moduleService.delete(moduleModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId")UUID courseId,
                                               @PathVariable(value = "moduleId")UUID moduleId,
                                               @RequestBody @Valid ModuleDto moduleDto){

        Optional<ModuleModel> moduleModelOptional = moduleService.findByModuleIdAndCourse(moduleId,courseId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }

        ModuleModel moduleModel = moduleModelOptional.get();
        moduleModel.setDescription(moduleDto.getDescription());
        moduleModel.setTitle(moduleDto.getTitle());
        moduleService.save(moduleModel);

        return ResponseEntity.ok().body(moduleModel);
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<List<ModuleModel>> getAllModules(@PathVariable(value = "courseId")UUID courseId){
        List<ModuleModel> all = moduleService.findAllModulesByCourse(courseId);
        return ResponseEntity.ok().body(all);
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getModule(@PathVariable(value = "courseId")UUID courseId,
                                            @PathVariable(value = "moduleId")UUID moduleId){
        Optional<ModuleModel> moduleModelOptional = moduleService.findByModuleIdAndCourse(moduleId,courseId);

        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }
        return ResponseEntity.ok().body(moduleModelOptional);
    }
}

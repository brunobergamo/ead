package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.speficication.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDTO courseDTO){

        LocalDateTime utf = LocalDateTime.now(ZoneId.of("UTC"));

        CourseModel courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO,courseModel);
        courseModel.setCreationDate(utf);
        courseModel.setLastUpdateDate(utf);
        courseService.save(courseModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(courseModel);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId")UUID courseId){

        Optional<CourseModel> courseModelOpt = courseService.findById(courseId);

        if(courseModelOpt.isPresent()){
            courseService.delete(courseModelOpt.get());
            return ResponseEntity.ok().body("Deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> UpdateCourse(@PathVariable(value = "courseId")UUID courseId,
                                               @RequestBody @Valid CourseDTO courseDTO){

        Optional<CourseModel> courseModelOpt = courseService.findById(courseId);

        if(courseModelOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }
        CourseModel courseModel = courseModelOpt.get();
        LocalDateTime utf = LocalDateTime.now(ZoneId.of("UTC"));
        courseModel.setLastUpdateDate(utf);
        courseModel.setDescription(courseDTO.getDescription());
        courseModel.setImageUrl(courseDTO.getImageUrl());
        courseModel.setCourseStatus(courseDTO.getCourseStatus());
        courseModel.setCourseLevel(courseDTO.getCourseLevel());
        courseService.save(courseModel);

        return ResponseEntity.ok().body(courseModel);
    }

    @GetMapping
    public ResponseEntity<List<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "courseId",
                                                                   direction = Sort.Direction.ASC)Pageable pageable){
        Page<CourseModel> all = courseService.findAll(spec,pageable);

        return ResponseEntity.ok().body(all.getContent());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getCourse(@PathVariable(value = "courseId")UUID courseId){
        Optional<CourseModel> courseModelOpt = courseService.findById(courseId);

        if(courseModelOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }
        return ResponseEntity.ok().body(courseModelOpt);
    }

}

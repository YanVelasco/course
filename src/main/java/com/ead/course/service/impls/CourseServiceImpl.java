package com.ead.course.service.impls;

import com.ead.course.controllers.CourseController;
import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.CoursePageDto;
import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.service.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CourseServiceImpl implements CourseService {

    final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        courseRepository.delete(courseModel);
    }

    @Transactional
    @Override
    public CourseModel save(CourseDto courseDto) {
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return courseRepository.save(courseModel);
    }

    @Override
    public Boolean existsByName(String name) {
        return courseRepository.existsByName(name);
    }

    @Transactional
    @Override
    public CoursePageDto findAll(Pageable pageable, String name, CourseStatus courseStatus, String description, CourseLevel courseLevel, UUID userInstructor) {

        Specification<CourseModel> spec = (root, query, cb)  -> cb.conjunction();

        if (name != null && !name.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (courseStatus != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("courseStatus"), courseStatus));
        }

        if (description != null && !description.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
        }

        if (courseLevel != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("courseLevel"), courseLevel));
        }

        if (userInstructor != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("userInstructor"), userInstructor));
        }

        var pageResult = courseRepository.findAll(spec, pageable);

        if (!pageResult.isEmpty()) {
            for (CourseModel courseModel : pageResult) {
                courseModel.add(linkTo(methodOn(CourseController.class).getCourseById(courseModel.getCourseId())).withSelfRel());
            }
        }

        return CoursePageDto.from(pageResult);

    }

    @Override
    public CourseModel findCourseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
    }

    @Transactional
    @Override
    public CourseModel updateCourse(CourseModel courseModel, CourseDto courseDto) {
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return courseRepository.save(courseModel);
    }

}

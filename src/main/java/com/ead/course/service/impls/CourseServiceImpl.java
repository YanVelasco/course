package com.ead.course.service.impls;

import com.ead.course.controllers.CourseController;
import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.CoursePageDto;
import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.CourseModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.service.CourseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(CourseServiceImpl.class);
    final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        logger.debug("Deleting course: {}", courseModel);
        courseRepository.delete(courseModel);
        logger.debug("Course deleted: {}", courseModel.getCourseId());
    }

    @Transactional
    @Override
    public CourseModel save(CourseDto courseDto) {
        logger.debug("Saving new course: {}", courseDto);
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        var saved = courseRepository.save(courseModel);
        logger.debug("Course saved: {}", saved);
        return saved;
    }

    @Override
    public Boolean existsByName(String name) {
        logger.debug("Checking if course exists by name: {}", name);
        return courseRepository.existsByName(name);
    }

    @Transactional
    @Override
    public CoursePageDto findAll(Pageable pageable, String name, CourseStatus courseStatus, String description, CourseLevel courseLevel, UUID userInstructor) {
        logger.debug("Finding all courses with filters - name: {}, courseStatus: {}, description: {}, courseLevel: {}, userInstructor: {}", name, courseStatus, description, courseLevel, userInstructor);
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

        logger.debug("Courses page result: {}", pageResult);
        return CoursePageDto.from(pageResult);
    }

    @Transactional
    @Override
    public CourseModel findCourseById(UUID courseId) {
        logger.debug("Finding course by id: {}", courseId);
        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Course with id %s not found", courseId)
                ));
        logger.debug("Course found: {}", course);
        return course;
    }

    @Transactional
    @Override
    public CourseModel updateCourse(CourseModel courseModel, CourseDto courseDto) {
        logger.debug("Updating course: {}, with data: {}", courseModel, courseDto);
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        var updated = courseRepository.save(courseModel);
        logger.debug("Course updated: {}", updated);
        return updated;
    }

}

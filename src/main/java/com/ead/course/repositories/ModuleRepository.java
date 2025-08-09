package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {

    ModuleModel findByTitle(String title);

    @EntityGraph(attributePaths = {"course"})
    @Query("SELECT m FROM ModuleModel m WHERE m.course.courseId = :courseId")
    List<ModuleModel> findByCourse(@Param("courseId") UUID courseId);

}

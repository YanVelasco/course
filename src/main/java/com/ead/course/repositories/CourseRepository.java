package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel> {

    Boolean existsByName(String name);

    @Query(value = "SELECT IF(COUNT(*) > 0, 1, 0) FROM tb_courses_users tcu WHERE tcu.course_id = :courseId AND tcu.user_id = :userId", nativeQuery = true)
    Integer existsByCourseAndUsersUser(@Param("courseId") UUID courseId, @Param("userId") UUID userId);

    @Modifying
    @Query(value = "DELETE FROM tb_courses_users WHERE course_id = :courseId", nativeQuery = true)
    void deleteCourseUserByCourse(@Param("courseId") UUID courseId);

    @Modifying
    @Query(value = "DELETE FROM tb_courses_users WHERE user_id = :userId", nativeQuery = true)
    void deleteCourseUserByUser(@Param("userId") UUID userId);

}

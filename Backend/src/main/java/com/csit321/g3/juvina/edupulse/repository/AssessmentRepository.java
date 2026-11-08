package com.csit321.g3.juvina.edupulse.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csit321.g3.juvina.edupulse.entity.Assessment;
import com.csit321.g3.juvina.edupulse.entity.ClassEntity;
import com.csit321.g3.juvina.edupulse.entity.GradeCategory;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {

    List<Assessment> findByClassEntity(ClassEntity classEntity);

    List<Assessment> findByClassEntityId(UUID classId);

    List<Assessment> findByGradeCategory(GradeCategory gradeCategory);

    List<Assessment> findByGradeCategoryId(UUID gradeCategoryId);

    @Query("SELECT a FROM Assessment a WHERE a.classEntity.id = :classId ORDER BY a.dueDate ASC, a.createdAt ASC")
    List<Assessment> findByClassIdOrderByDueDate(@Param("classId") UUID classId);

    @Query("SELECT a FROM Assessment a WHERE a.classEntity.id = :classId AND a.isPublished = true ORDER BY a.dueDate ASC")
    List<Assessment> findPublishedByClassId(@Param("classId") UUID classId);

    Optional<Assessment> findByIdAndClassEntityId(UUID assessmentId, UUID classId);

    Page<Assessment> findByClassEntityId(UUID classId, Pageable pageable);

    boolean existsByTitleAndClassEntityId(String title, UUID classId);
}
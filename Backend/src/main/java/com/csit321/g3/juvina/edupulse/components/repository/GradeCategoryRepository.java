package com.csit321.g3.juvina.edupulse.components.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csit321.g3.juvina.edupulse.components.entity.ClassEntity;
import com.csit321.g3.juvina.edupulse.components.entity.GradeCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GradeCategoryRepository extends JpaRepository<GradeCategory, UUID> {

    List<GradeCategory> findByClassEntity(ClassEntity classEntity);

    List<GradeCategory> findByClassEntityId(UUID classId);

    @Query("SELECT gc FROM GradeCategory gc WHERE gc.classEntity.id = :classId ORDER BY gc.displayOrder ASC, gc.createdAt ASC")
    List<GradeCategory> findByClassIdOrderByDisplayOrder(@Param("classId") UUID classId);

    Optional<GradeCategory> findByIdAndClassEntityId(UUID categoryId, UUID classId);

    @Query("SELECT SUM(gc.weightPercentage) FROM GradeCategory gc WHERE gc.classEntity.id = :classId")
    BigDecimal getTotalWeightByClassId(@Param("classId") UUID classId);

    boolean existsByNameAndClassEntityId(String name, UUID classId);
}
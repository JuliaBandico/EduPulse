package com.csit321.g3.juvina.edupulse.components.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csit321.g3.juvina.edupulse.components.entity.ScoreEntity;

@Repository
public interface ScoreRepository extends JpaRepository<ScoreEntity, UUID> {

    List<ScoreEntity> findByStudentId(UUID studentId);

    List<ScoreEntity> findByAssessmentId(UUID assessmentId);

    Optional<ScoreEntity> findByStudentIdAndAssessmentId(UUID studentId, UUID assessmentId);

    boolean existsByStudentIdAndAssessmentId(UUID studentId, UUID assessmentId);
}
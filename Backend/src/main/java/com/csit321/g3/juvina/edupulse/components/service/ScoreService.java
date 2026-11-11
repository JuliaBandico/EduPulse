package com.csit321.g3.juvina.edupulse.components.service;

import com.csit321.g3.juvina.edupulse.components.dto.ScoreDto;
import com.csit321.g3.juvina.edupulse.components.entity.Assessment;
import com.csit321.g3.juvina.edupulse.components.entity.ScoreEntity;
import com.csit321.g3.juvina.edupulse.components.entity.User;
import com.csit321.g3.juvina.edupulse.components.repository.AssessmentRepository;
import com.csit321.g3.juvina.edupulse.components.repository.ScoreRepository;
import com.csit321.g3.juvina.edupulse.components.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;

    public ScoreService(ScoreRepository scoreRepository,
                        UserRepository userRepository,
                        AssessmentRepository assessmentRepository) {
        this.scoreRepository = scoreRepository;
        this.userRepository = userRepository;
        this.assessmentRepository = assessmentRepository;
    }

    @Transactional
    public ScoreDto.Response create(ScoreDto.CreateRequest request) {
        User student = userRepository.findById(request.studentId())
                .orElseThrow(() -> notFound("Student not found"));

        if (!Boolean.TRUE.equals(student.getIsStudent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a student");
        }

        Assessment assessment = assessmentRepository.findById(request.assessmentId())
                .orElseThrow(() -> notFound("Assessment not found"));

        if (scoreRepository.existsByStudentIdAndAssessmentId(student.getId(), assessment.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This student already has a score for this assessment");
        }

        checkNotAboveMax(request.scoreValue(), assessment);

        ScoreEntity score = ScoreEntity.builder()
                .student(student)
                .assessment(assessment)
                .scoreValue(request.scoreValue())
                .feedback(request.feedback())
                .build();

        return ScoreDto.Response.from(scoreRepository.save(score));
    }

    @Transactional(readOnly = true)
    public ScoreDto.Response getById(UUID id) {
        return ScoreDto.Response.from(findScore(id));
    }

    @Transactional(readOnly = true)
    public List<ScoreDto.Response> getByStudent(UUID studentId) {
        return scoreRepository.findByStudentId(studentId).stream()
                .map(ScoreDto.Response::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScoreDto.Response> getByAssessment(UUID assessmentId) {
        return scoreRepository.findByAssessmentId(assessmentId).stream()
                .map(ScoreDto.Response::from)
                .toList();
    }

    @Transactional
    public ScoreDto.Response update(UUID id, ScoreDto.UpdateRequest request) {
        ScoreEntity score = findScore(id);

        checkNotAboveMax(request.scoreValue(), score.getAssessment());

        score.setScoreValue(request.scoreValue());
        score.setFeedback(request.feedback());

        return ScoreDto.Response.from(scoreRepository.save(score));
    }

    @Transactional
    public void delete(UUID id) {
        scoreRepository.delete(findScore(id));
    }

    // ---------- helpers ----------

    private ScoreEntity findScore(UUID id) {
        return scoreRepository.findById(id).orElseThrow(() -> notFound("Score not found"));
    }

    private void checkNotAboveMax(BigDecimal value, Assessment assessment) {
        if (value.compareTo(assessment.getMaxScore()) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Score cannot be higher than the max score (" + assessment.getMaxScore() + ")");
        }
    }

    private ResponseStatusException notFound(String message) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }
}
package com.csit321.g3.juvina.edupulse.components.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.csit321.g3.juvina.edupulse.components.entity.ScoreEntity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public final class ScoreDto {

    private ScoreDto() {}

    // Used when a teacher gives a score for the first time.
    public record CreateRequest(
            @NotNull UUID studentId,
            @NotNull UUID assessmentId,
            @NotNull @DecimalMin("0.0") BigDecimal scoreValue,
            String feedback
    ) {}

    // Used when a teacher changes an existing score.
    public record UpdateRequest(
            @NotNull @DecimalMin("0.0") BigDecimal scoreValue,
            String feedback
    ) {}

    // What we send back to the frontend.
    public record Response(
            UUID id,
            UUID studentId,
            String studentName,
            UUID assessmentId,
            String assessmentTitle,
            BigDecimal maxScore,
            BigDecimal scoreValue,
            String feedback,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static Response from(ScoreEntity s) {
            return new Response(
                    s.getId(),
                    s.getStudent().getId(),
                    s.getStudent().getFullName(),
                    s.getAssessment().getId(),
                    s.getAssessment().getTitle(),
                    s.getAssessment().getMaxScore(),
                    s.getScoreValue(),
                    s.getFeedback(),
                    s.getCreatedAt(),
                    s.getUpdatedAt()
            );
        }
    }
}
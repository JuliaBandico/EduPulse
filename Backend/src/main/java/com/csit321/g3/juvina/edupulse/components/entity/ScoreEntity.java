package com.csit321.g3.juvina.edupulse.components.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "scores",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_scores_student_assessment",
        columnNames = {"student_id", "assessment_id"}
    )
)
public class ScoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "score_value", nullable = false, precision = 6, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal scoreValue;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ScoreEntity() {}

    public ScoreEntity(UUID id, BigDecimal scoreValue, String feedback,
                 User student, Assessment assessment,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.scoreValue = scoreValue;
        this.feedback = feedback;
        this.student = student;
        this.assessment = assessment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ScoreBuilder builder() {
        return new ScoreBuilder();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public BigDecimal getScoreValue() { return scoreValue; }
    public void setScoreValue(BigDecimal scoreValue) { this.scoreValue = scoreValue; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Assessment getAssessment() { return assessment; }
    public void setAssessment(Assessment assessment) { this.assessment = assessment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        validateScore();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        validateScore();
    }

    // A score can't be higher than the assessment's perfect score.
    private void validateScore() {
        if (scoreValue != null && assessment != null && assessment.getMaxScore() != null
                && scoreValue.compareTo(assessment.getMaxScore()) > 0) {
            throw new IllegalStateException("Score cannot be higher than the assessment's max score.");
        }
    }

    public static class ScoreBuilder {
        private UUID id;
        private BigDecimal scoreValue;
        private String feedback;
        private User student;
        private Assessment assessment;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ScoreBuilder id(UUID id) { this.id = id; return this; }
        public ScoreBuilder scoreValue(BigDecimal scoreValue) { this.scoreValue = scoreValue; return this; }
        public ScoreBuilder feedback(String feedback) { this.feedback = feedback; return this; }
        public ScoreBuilder student(User student) { this.student = student; return this; }
        public ScoreBuilder assessment(Assessment assessment) { this.assessment = assessment; return this; }
        public ScoreBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ScoreBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ScoreEntity build() {
            return new ScoreEntity(id, scoreValue, feedback, student, assessment, createdAt, updatedAt);
        }
    }
}
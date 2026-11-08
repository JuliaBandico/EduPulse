package com.csit321.g3.juvina.edupulse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private AssessmentType type;

    @Column(name = "max_score", nullable = false, precision = 6, scale = 2)
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal maxScore;

    @Column(name = "weight_within_category", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true)
    @Max(100)
    private BigDecimal weightWithinCategory;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_category_id", nullable = false)
    private GradeCategory gradeCategory;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Assessment() {}

    public Assessment(UUID id, String title, String description, AssessmentType type,
                      BigDecimal maxScore, BigDecimal weightWithinCategory,
                      LocalDateTime dueDate, Boolean isPublished,
                      ClassEntity classEntity, GradeCategory gradeCategory,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.maxScore = maxScore;
        this.weightWithinCategory = weightWithinCategory;
        this.dueDate = dueDate;
        this.isPublished = isPublished;
        this.classEntity = classEntity;
        this.gradeCategory = gradeCategory;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AssessmentBuilder builder() {
        return new AssessmentBuilder();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public AssessmentType getType() { return type; }
    public void setType(AssessmentType type) { this.type = type; }

    public BigDecimal getMaxScore() { return maxScore; }
    public void setMaxScore(BigDecimal maxScore) { this.maxScore = maxScore; }

    public BigDecimal getWeightWithinCategory() { return weightWithinCategory; }
    public void setWeightWithinCategory(BigDecimal weightWithinCategory) { this.weightWithinCategory = weightWithinCategory; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }

    public ClassEntity getClassEntity() { return classEntity; }
    public void setClassEntity(ClassEntity classEntity) { this.classEntity = classEntity; }

    public GradeCategory getGradeCategory() { return gradeCategory; }
    public void setGradeCategory(GradeCategory gradeCategory) { this.gradeCategory = gradeCategory; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.weightWithinCategory == null) {
            this.weightWithinCategory = BigDecimal.valueOf(100);
        }
        if (this.isPublished == null) {
            this.isPublished = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum AssessmentType {
        QUIZ,
        EXAM,
        ASSIGNMENT,
        PROJECT,
        PARTICIPATION,
        OTHER
    }

    public static class AssessmentBuilder {
        private UUID id;
        private String title;
        private String description;
        private AssessmentType type;
        private BigDecimal maxScore;
        private BigDecimal weightWithinCategory;
        private LocalDateTime dueDate;
        private Boolean isPublished;
        private ClassEntity classEntity;
        private GradeCategory gradeCategory;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public AssessmentBuilder id(UUID id) { this.id = id; return this; }
        public AssessmentBuilder title(String title) { this.title = title; return this; }
        public AssessmentBuilder description(String description) { this.description = description; return this; }
        public AssessmentBuilder type(AssessmentType type) { this.type = type; return this; }
        public AssessmentBuilder maxScore(BigDecimal maxScore) { this.maxScore = maxScore; return this; }
        public AssessmentBuilder weightWithinCategory(BigDecimal weightWithinCategory) { this.weightWithinCategory = weightWithinCategory; return this; }
        public AssessmentBuilder dueDate(LocalDateTime dueDate) { this.dueDate = dueDate; return this; }
        public AssessmentBuilder isPublished(Boolean isPublished) { this.isPublished = isPublished; return this; }
        public AssessmentBuilder classEntity(ClassEntity classEntity) { this.classEntity = classEntity; return this; }
        public AssessmentBuilder gradeCategory(GradeCategory gradeCategory) { this.gradeCategory = gradeCategory; return this; }
        public AssessmentBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AssessmentBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Assessment build() {
            return new Assessment(id, title, description, type, maxScore, weightWithinCategory,
                    dueDate, isPublished, classEntity, gradeCategory, createdAt, updatedAt);
        }
    }
}
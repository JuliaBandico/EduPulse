package com.csit321.g3.juvina.edupulse.components.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AssessmentDto {

    private UUID id;

    @NotBlank(message = "Assessment title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Assessment type is required")
    private AssessmentType type;

    @NotNull(message = "Max score is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Max score must be greater than 0")
    private BigDecimal maxScore;

    @DecimalMin(value = "0.0", inclusive = true, message = "Weight within category must be >= 0")
    @Max(value = 100, message = "Weight within category must not exceed 100")
    private BigDecimal weightWithinCategory;

    private LocalDateTime dueDate;

    private Boolean isPublished;

    @NotNull(message = "Class ID is required")
    private UUID classId;

    @NotNull(message = "Grade Category ID is required")
    private UUID gradeCategoryId;

    private String className;
    private String gradeCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AssessmentDto() {}

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

    public UUID getClassId() { return classId; }
    public void setClassId(UUID classId) { this.classId = classId; }

    public UUID getGradeCategoryId() { return gradeCategoryId; }
    public void setGradeCategoryId(UUID gradeCategoryId) { this.gradeCategoryId = gradeCategoryId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getGradeCategoryName() { return gradeCategoryName; }
    public void setGradeCategoryName(String gradeCategoryName) { this.gradeCategoryName = gradeCategoryName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum AssessmentType {
        QUIZ, EXAM, ASSIGNMENT, PROJECT, PARTICIPATION, OTHER
    }
}
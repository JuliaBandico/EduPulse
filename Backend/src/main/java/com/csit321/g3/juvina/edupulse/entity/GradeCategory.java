package com.csit321.g3.juvina.edupulse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "grade_categories")
public class GradeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "weight_percentage", nullable = false, precision = 5, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true)
    @Max(100)
    private BigDecimal weightPercentage;

    @Column(name = "display_order")
    @Min(0)
    private Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public GradeCategory() {}

    public GradeCategory(UUID id, String name, String description, BigDecimal weightPercentage,
                         Integer displayOrder, ClassEntity classEntity,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.weightPercentage = weightPercentage;
        this.displayOrder = displayOrder;
        this.classEntity = classEntity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GradeCategoryBuilder builder() {
        return new GradeCategoryBuilder();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getWeightPercentage() { return weightPercentage; }
    public void setWeightPercentage(BigDecimal weightPercentage) { this.weightPercentage = weightPercentage; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public ClassEntity getClassEntity() { return classEntity; }
    public void setClassEntity(ClassEntity classEntity) { this.classEntity = classEntity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.displayOrder == null) {
            this.displayOrder = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static class GradeCategoryBuilder {
        private UUID id;
        private String name;
        private String description;
        private BigDecimal weightPercentage;
        private Integer displayOrder;
        private ClassEntity classEntity;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public GradeCategoryBuilder id(UUID id) { this.id = id; return this; }
        public GradeCategoryBuilder name(String name) { this.name = name; return this; }
        public GradeCategoryBuilder description(String description) { this.description = description; return this; }
        public GradeCategoryBuilder weightPercentage(BigDecimal weightPercentage) { this.weightPercentage = weightPercentage; return this; }
        public GradeCategoryBuilder displayOrder(Integer displayOrder) { this.displayOrder = displayOrder; return this; }
        public GradeCategoryBuilder classEntity(ClassEntity classEntity) { this.classEntity = classEntity; return this; }
        public GradeCategoryBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public GradeCategoryBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public GradeCategory build() {
            return new GradeCategory(id, name, description, weightPercentage, displayOrder, classEntity, createdAt, updatedAt);
        }
    }
}
package com.csit321.g3.juvina.edupulse.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "classes")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "subject", length = 100)
    private String subject;

    @Column(name = "section", length = 20)
    private String section;

    @Column(name = "school_year", length = 20)
    private String schoolYear;

    @Column(name = "semester", length = 20)
    private String semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ClassEntity() {}

    public ClassEntity(UUID id, String name, String description, String subject, String section,
                       String schoolYear, String semester, User teacher,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.section = section;
        this.schoolYear = schoolYear;
        this.semester = semester;
        this.teacher = teacher;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ClassEntityBuilder builder() {
        return new ClassEntityBuilder();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public User getTeacher() { return teacher; }
    public void setTeacher(User teacher) { this.teacher = teacher; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static class ClassEntityBuilder {
        private UUID id;
        private String name;
        private String description;
        private String subject;
        private String section;
        private String schoolYear;
        private String semester;
        private User teacher;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ClassEntityBuilder id(UUID id) { this.id = id; return this; }
        public ClassEntityBuilder name(String name) { this.name = name; return this; }
        public ClassEntityBuilder description(String description) { this.description = description; return this; }
        public ClassEntityBuilder subject(String subject) { this.subject = subject; return this; }
        public ClassEntityBuilder section(String section) { this.section = section; return this; }
        public ClassEntityBuilder schoolYear(String schoolYear) { this.schoolYear = schoolYear; return this; }
        public ClassEntityBuilder semester(String semester) { this.semester = semester; return this; }
        public ClassEntityBuilder teacher(User teacher) { this.teacher = teacher; return this; }
        public ClassEntityBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ClassEntityBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ClassEntity build() {
            return new ClassEntity(id, name, description, subject, section, schoolYear, semester, teacher, createdAt, updatedAt);
        }
    }
}
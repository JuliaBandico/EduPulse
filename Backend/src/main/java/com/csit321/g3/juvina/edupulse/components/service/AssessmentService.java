package com.csit321.g3.juvina.edupulse.components.service;

import com.csit321.g3.juvina.edupulse.components.dto.AssessmentDto;
import com.csit321.g3.juvina.edupulse.components.entity.Assessment;
import com.csit321.g3.juvina.edupulse.components.entity.ClassEntity;
import com.csit321.g3.juvina.edupulse.components.entity.GradeCategory;
import com.csit321.g3.juvina.edupulse.components.repository.AssessmentRepository;
import com.csit321.g3.juvina.edupulse.components.repository.ClassRepository;
import com.csit321.g3.juvina.edupulse.components.repository.GradeCategoryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final ClassRepository classRepository;
    private final GradeCategoryRepository gradeCategoryRepository;

    public AssessmentService(AssessmentRepository assessmentRepository, ClassRepository classRepository,
                             GradeCategoryRepository gradeCategoryRepository) {
        this.assessmentRepository = assessmentRepository;
        this.classRepository = classRepository;
        this.gradeCategoryRepository = gradeCategoryRepository;
    }

    // CREATE
    public AssessmentDto createAssessment(AssessmentDto dto) {
        // Verify class exists
        ClassEntity classEntity = classRepository.findById(dto.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found with ID: " + dto.getClassId()));

        // Verify grade category exists and belongs to this class
        GradeCategory gradeCategory = gradeCategoryRepository.findByIdAndClassEntityId(dto.getGradeCategoryId(), dto.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Grade category not found or doesn't belong to this class"));

        // Check for duplicate assessment title in this class
        if (assessmentRepository.existsByTitleAndClassEntityId(dto.getTitle(), dto.getClassId())) {
            throw new IllegalArgumentException("Assessment with title '" + dto.getTitle() + "' already exists in this class");
        }

        Assessment assessment = Assessment.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .type(Assessment.AssessmentType.valueOf(dto.getType().name()))
                .maxScore(dto.getMaxScore())
                .weightWithinCategory(dto.getWeightWithinCategory() != null ? dto.getWeightWithinCategory() : java.math.BigDecimal.valueOf(100))
                .dueDate(dto.getDueDate())
                .isPublished(dto.getIsPublished() != null ? dto.getIsPublished() : false)
                .classEntity(classEntity)
                .gradeCategory(gradeCategory)
                .build();

        Assessment saved = assessmentRepository.save(assessment);
        return mapToDto(saved);
    }

    // READ - Get by ID
    @Transactional(readOnly = true)
    public Optional<AssessmentDto> getAssessmentById(UUID id) {
        return assessmentRepository.findById(id).map(this::mapToDto);
    }

    // READ - Get all assessments for a class
    @Transactional(readOnly = true)
    public List<AssessmentDto> getAssessmentsByClass(UUID classId) {
        return assessmentRepository.findByClassIdOrderByDueDate(classId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // READ - Get published assessments for a class (for students)
    @Transactional(readOnly = true)
    public List<AssessmentDto> getPublishedAssessmentsByClass(UUID classId) {
        return assessmentRepository.findPublishedByClassId(classId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // READ - Get assessments by grade category
    @Transactional(readOnly = true)
    public List<AssessmentDto> getAssessmentsByGradeCategory(UUID gradeCategoryId) {
        return assessmentRepository.findByGradeCategoryId(gradeCategoryId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // READ - Get assessment by ID and class (for authorization)
    @Transactional(readOnly = true)
    public Optional<AssessmentDto> getAssessmentByIdAndClass(UUID assessmentId, UUID classId) {
        return assessmentRepository.findByIdAndClassEntityId(assessmentId, classId).map(this::mapToDto);
    }

    // READ - Paginated assessments for a class
    @Transactional(readOnly = true)
    public Page<AssessmentDto> getAssessmentsByClassPaginated(UUID classId, Pageable pageable) {
        return assessmentRepository.findByClassEntityId(classId, pageable).map(this::mapToDto);
    }

    // UPDATE
    public Optional<AssessmentDto> updateAssessment(UUID assessmentId, AssessmentDto dto) {
        return assessmentRepository.findById(assessmentId).map(existingAssessment -> {
            ClassEntity classEntity = classRepository.findById(dto.getClassId())
                    .orElseThrow(() -> new IllegalArgumentException("Class not found with ID: " + dto.getClassId()));

            if (!classEntity.getId().equals(existingAssessment.getClassEntity().getId())) {
                throw new IllegalArgumentException("Assessment does not belong to class: " + dto.getClassId());
            }

            GradeCategory gradeCategory = gradeCategoryRepository.findByIdAndClassEntityId(dto.getGradeCategoryId(), dto.getClassId())
                    .orElseThrow(() -> new IllegalArgumentException("Grade category not found or doesn't belong to this class"));

            if (dto.getTitle() != null && !dto.getTitle().isBlank()
                    && !dto.getTitle().equals(existingAssessment.getTitle())
                    && assessmentRepository.existsByTitleAndClassEntityId(dto.getTitle(), dto.getClassId())) {
                throw new IllegalArgumentException("Assessment with title '" + dto.getTitle() + "' already exists in this class");
            }

            if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
                existingAssessment.setTitle(dto.getTitle());
            }
            if (dto.getDescription() != null) {
                existingAssessment.setDescription(dto.getDescription());
            }
            if (dto.getType() != null) {
                existingAssessment.setType(Assessment.AssessmentType.valueOf(dto.getType().name()));
            }
            if (dto.getMaxScore() != null) {
                existingAssessment.setMaxScore(dto.getMaxScore());
            }
            if (dto.getWeightWithinCategory() != null) {
                existingAssessment.setWeightWithinCategory(dto.getWeightWithinCategory());
            }
            if (dto.getDueDate() != null) {
                existingAssessment.setDueDate(dto.getDueDate());
            }
            if (dto.getIsPublished() != null) {
                existingAssessment.setIsPublished(dto.getIsPublished());
            }

            existingAssessment.setClassEntity(classEntity);
            existingAssessment.setGradeCategory(gradeCategory);

            return mapToDto(assessmentRepository.save(existingAssessment));
        });
    }

    // DELETE
    public boolean deleteAssessment(UUID id) {
        if (!assessmentRepository.existsById(id)) {
            return false;
        }
        assessmentRepository.deleteById(id);
        return true;
    }

    private AssessmentDto mapToDto(Assessment assessment) {
        AssessmentDto dto = new AssessmentDto();
        dto.setId(assessment.getId());
        dto.setTitle(assessment.getTitle());
        dto.setDescription(assessment.getDescription());
        dto.setType(AssessmentDto.AssessmentType.valueOf(assessment.getType().name()));
        dto.setMaxScore(assessment.getMaxScore());
        dto.setWeightWithinCategory(assessment.getWeightWithinCategory());
        dto.setDueDate(assessment.getDueDate());
        dto.setIsPublished(assessment.getIsPublished());
        dto.setClassId(assessment.getClassEntity().getId());
        dto.setClassName(assessment.getClassEntity().getName());
        dto.setGradeCategoryId(assessment.getGradeCategory().getId());
        dto.setGradeCategoryName(assessment.getGradeCategory().getName());
        dto.setCreatedAt(assessment.getCreatedAt());
        dto.setUpdatedAt(assessment.getUpdatedAt());
        return dto;
    }
}
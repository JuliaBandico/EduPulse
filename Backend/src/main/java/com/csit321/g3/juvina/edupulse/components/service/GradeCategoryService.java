package com.csit321.g3.juvina.edupulse.components.service;

import com.csit321.g3.juvina.edupulse.components.dto.GradeCategoryDto;
import com.csit321.g3.juvina.edupulse.components.entity.ClassEntity;
import com.csit321.g3.juvina.edupulse.components.entity.GradeCategory;
import com.csit321.g3.juvina.edupulse.components.repository.ClassRepository;
import com.csit321.g3.juvina.edupulse.components.repository.GradeCategoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class GradeCategoryService {

    private final GradeCategoryRepository gradeCategoryRepository;
    private final ClassRepository classRepository;

    public GradeCategoryService(GradeCategoryRepository gradeCategoryRepository, ClassRepository classRepository) {
        this.gradeCategoryRepository = gradeCategoryRepository;
        this.classRepository = classRepository;
    }

    // CREATE
    public GradeCategoryDto createGradeCategory(GradeCategoryDto dto) {
        // Verify class exists
        ClassEntity classEntity = classRepository.findById(dto.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found with ID: " + dto.getClassId()));

        // Check for duplicate category name in this class
        if (gradeCategoryRepository.existsByNameAndClassEntityId(dto.getName(), dto.getClassId())) {
            throw new IllegalArgumentException("Grade category with name '" + dto.getName() + "' already exists in this class");
        }

        // Validate total weight doesn't exceed 100%
        BigDecimal currentTotal = gradeCategoryRepository.getTotalWeightByClassId(dto.getClassId());
        BigDecimal newTotal = (currentTotal != null ? currentTotal : BigDecimal.ZERO).add(dto.getWeightPercentage());
        if (newTotal.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Total weight percentage would exceed 100%. Current total: " + currentTotal + "%, adding: " + dto.getWeightPercentage() + "%");
        }

        GradeCategory category = GradeCategory.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .weightPercentage(dto.getWeightPercentage())
                .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
                .classEntity(classEntity)
                .build();

        GradeCategory saved = gradeCategoryRepository.save(category);
        return mapToDto(saved);
    }

    // READ - Get by ID
    @Transactional(readOnly = true)
    public Optional<GradeCategoryDto> getGradeCategoryById(UUID id) {
        return gradeCategoryRepository.findById(id).map(this::mapToDto);
    }

    // READ - Get all categories for a class
    @Transactional(readOnly = true)
    public List<GradeCategoryDto> getGradeCategoriesByClass(UUID classId) {
        return gradeCategoryRepository.findByClassIdOrderByDisplayOrder(classId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // READ - Get category by ID and class (for authorization)
    @Transactional(readOnly = true)
    public Optional<GradeCategoryDto> getGradeCategoryByIdAndClass(UUID categoryId, UUID classId) {
        return gradeCategoryRepository.findByIdAndClassEntityId(categoryId, classId).map(this::mapToDto);
    }

    // READ - Get total weight for a class
    @Transactional(readOnly = true)
    public BigDecimal getTotalWeightByClass(UUID classId) {
        BigDecimal total = gradeCategoryRepository.getTotalWeightByClassId(classId);
        return total != null ? total : BigDecimal.ZERO;
    }

    // UPDATE
    public Optional<GradeCategoryDto> updateGradeCategory(UUID categoryId, UUID classId, GradeCategoryDto dto) {
        return gradeCategoryRepository.findByIdAndClassEntityId(categoryId, classId).map(existingCategory -> {
            BigDecimal currentTotal = gradeCategoryRepository.getTotalWeightByClassId(classId);
            BigDecimal newWeight = dto.getWeightPercentage() != null ? dto.getWeightPercentage() : existingCategory.getWeightPercentage();

            if (dto.getName() != null && !dto.getName().isBlank()
                    && !dto.getName().equals(existingCategory.getName())
                    && gradeCategoryRepository.existsByNameAndClassEntityId(dto.getName(), classId)) {
                throw new IllegalArgumentException("Grade category with name '" + dto.getName() + "' already exists in this class");
            }

            BigDecimal projectedTotal = (currentTotal != null ? currentTotal : BigDecimal.ZERO)
                    .subtract(existingCategory.getWeightPercentage())
                    .add(newWeight);
            if (projectedTotal.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new IllegalArgumentException("Total weight percentage would exceed 100%. Current total: "
                        + currentTotal + "% after update: " + projectedTotal + "%");
            }

            if (dto.getName() != null && !dto.getName().isBlank()) {
                existingCategory.setName(dto.getName());
            }
            if (dto.getDescription() != null) {
                existingCategory.setDescription(dto.getDescription());
            }
            if (dto.getWeightPercentage() != null) {
                existingCategory.setWeightPercentage(dto.getWeightPercentage());
            }
            if (dto.getDisplayOrder() != null) {
                existingCategory.setDisplayOrder(dto.getDisplayOrder());
            }

            return mapToDto(gradeCategoryRepository.save(existingCategory));
        });
    }

    // DELETE
    public boolean deleteGradeCategory(UUID categoryId, UUID classId) {
        if (gradeCategoryRepository.findByIdAndClassEntityId(categoryId, classId).isEmpty()) {
            return false;
        }
        gradeCategoryRepository.deleteById(categoryId);
        return true;
    }

    private GradeCategoryDto mapToDto(GradeCategory category) {
        GradeCategoryDto dto = new GradeCategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setWeightPercentage(category.getWeightPercentage());
        dto.setDisplayOrder(category.getDisplayOrder());
        dto.setClassId(category.getClassEntity().getId());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }
}
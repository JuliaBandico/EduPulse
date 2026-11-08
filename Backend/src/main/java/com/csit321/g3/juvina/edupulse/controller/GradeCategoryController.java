package com.csit321.g3.juvina.edupulse.controller;

import com.csit321.g3.juvina.edupulse.dto.GradeCategoryDto;
import com.csit321.g3.juvina.edupulse.service.GradeCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/grade-categories")
public class GradeCategoryController {

    private final GradeCategoryService gradeCategoryService;

    public GradeCategoryController(GradeCategoryService gradeCategoryService) {
        this.gradeCategoryService = gradeCategoryService;
    }

    // CREATE - Create a new grade category
    @PostMapping
    public ResponseEntity<GradeCategoryDto> createGradeCategory(@Valid @RequestBody GradeCategoryDto dto) {
        GradeCategoryDto created = gradeCategoryService.createGradeCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ - Get grade category by ID
    @GetMapping("/{id}")
    public ResponseEntity<GradeCategoryDto> getGradeCategoryById(@PathVariable UUID id) {
        return gradeCategoryService.getGradeCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Get all grade categories for a class
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<GradeCategoryDto>> getGradeCategoriesByClass(@PathVariable UUID classId) {
        List<GradeCategoryDto> categories = gradeCategoryService.getGradeCategoriesByClass(classId);
        return ResponseEntity.ok(categories);
    }

    // READ - Get grade category by ID and class (for authorization)
    @GetMapping("/{id}/class/{classId}")
    public ResponseEntity<GradeCategoryDto> getGradeCategoryByIdAndClass(@PathVariable UUID id, @PathVariable UUID classId) {
        return gradeCategoryService.getGradeCategoryByIdAndClass(id, classId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Get total weight percentage for a class
    @GetMapping("/class/{classId}/total-weight")
    public ResponseEntity<BigDecimal> getTotalWeightByClass(@PathVariable UUID classId) {
        BigDecimal total = gradeCategoryService.getTotalWeightByClass(classId);
        return ResponseEntity.ok(total);
    }
}
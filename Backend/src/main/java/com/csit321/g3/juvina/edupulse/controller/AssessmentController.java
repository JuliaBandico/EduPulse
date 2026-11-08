package com.csit321.g3.juvina.edupulse.controller;

import com.csit321.g3.juvina.edupulse.dto.AssessmentDto;
import com.csit321.g3.juvina.edupulse.service.AssessmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    // CREATE - Create a new assessment
    @PostMapping
    public ResponseEntity<AssessmentDto> createAssessment(@Valid @RequestBody AssessmentDto dto) {
        AssessmentDto created = assessmentService.createAssessment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ - Get assessment by ID
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentDto> getAssessmentById(@PathVariable UUID id) {
        return assessmentService.getAssessmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Get all assessments for a class
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<AssessmentDto>> getAssessmentsByClass(@PathVariable UUID classId) {
        List<AssessmentDto> assessments = assessmentService.getAssessmentsByClass(classId);
        return ResponseEntity.ok(assessments);
    }

    // READ - Get published assessments for a class (for students)
    @GetMapping("/class/{classId}/published")
    public ResponseEntity<List<AssessmentDto>> getPublishedAssessmentsByClass(@PathVariable UUID classId) {
        List<AssessmentDto> assessments = assessmentService.getPublishedAssessmentsByClass(classId);
        return ResponseEntity.ok(assessments);
    }

    // READ - Get assessments by grade category
    @GetMapping("/grade-category/{gradeCategoryId}")
    public ResponseEntity<List<AssessmentDto>> getAssessmentsByGradeCategory(@PathVariable UUID gradeCategoryId) {
        List<AssessmentDto> assessments = assessmentService.getAssessmentsByGradeCategory(gradeCategoryId);
        return ResponseEntity.ok(assessments);
    }

    // READ - Get assessment by ID and class (for authorization)
    @GetMapping("/{id}/class/{classId}")
    public ResponseEntity<AssessmentDto> getAssessmentByIdAndClass(@PathVariable UUID id, @PathVariable UUID classId) {
        return assessmentService.getAssessmentByIdAndClass(id, classId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Get paginated assessments for a class
    @GetMapping("/class/{classId}/paginated")
    public ResponseEntity<Page<AssessmentDto>> getAssessmentsByClassPaginated(
            @PathVariable UUID classId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssessmentDto> assessments = assessmentService.getAssessmentsByClassPaginated(classId, pageable);
        return ResponseEntity.ok(assessments);
    }
}
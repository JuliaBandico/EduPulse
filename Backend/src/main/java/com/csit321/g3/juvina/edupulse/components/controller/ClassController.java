package com.csit321.g3.juvina.edupulse.components.controller;

import com.csit321.g3.juvina.edupulse.components.dto.ClassDto;
import com.csit321.g3.juvina.edupulse.components.service.ClassService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    // CREATE - Create a new class
    @PostMapping
    public ResponseEntity<ClassDto> createClass(@Valid @RequestBody ClassDto classDto) {
        ClassDto created = classService.createClass(classDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ - Get class by ID
    @GetMapping("/{id}")
    public ResponseEntity<ClassDto> getClassById(@PathVariable UUID id) {
        return classService.getClassById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Get all classes
    @GetMapping
    public ResponseEntity<List<ClassDto>> getAllClasses() {
        List<ClassDto> classes = classService.getAllClasses();
        return ResponseEntity.ok(classes);
    }

    // READ - Get classes by teacher
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<ClassDto>> getClassesByTeacher(@PathVariable UUID teacherId) {
        List<ClassDto> classes = classService.getClassesByTeacher(teacherId);
        return ResponseEntity.ok(classes);
    }

    // READ - Get class by ID and teacher (for authorization)
    @GetMapping("/{id}/teacher/{teacherId}")
    public ResponseEntity<ClassDto> getClassByIdAndTeacher(@PathVariable UUID id, @PathVariable UUID teacherId) {
        return classService.getClassByIdAndTeacher(id, teacherId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE - Update an existing class
    @PutMapping("/{id}")
    public ResponseEntity<ClassDto> updateClass(@PathVariable UUID id, @Valid @RequestBody ClassDto classDto) {
        return classService.updateClass(id, classDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Delete a class
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable UUID id) {
        return classService.deleteClass(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
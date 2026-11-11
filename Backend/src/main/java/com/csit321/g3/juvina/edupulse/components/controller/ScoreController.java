package com.csit321.g3.juvina.edupulse.components.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csit321.g3.juvina.edupulse.components.dto.ScoreDto;
import com.csit321.g3.juvina.edupulse.components.service.ScoreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    // POST /api/scores  -> give a score
    @PostMapping
    public ResponseEntity<ScoreDto.Response> create(@Valid @RequestBody ScoreDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scoreService.create(request));
    }

    // GET /api/scores/{id}  -> get one score
    @GetMapping("/{id}")
    public ResponseEntity<ScoreDto.Response> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(scoreService.getById(id));
    }

    // GET /api/scores/student/{studentId}  -> all scores of one student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ScoreDto.Response>> getByStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(scoreService.getByStudent(studentId));
    }

    // GET /api/scores/assessment/{assessmentId}  -> all scores of one activity
    @GetMapping("/assessment/{assessmentId}")
    public ResponseEntity<List<ScoreDto.Response>> getByAssessment(@PathVariable UUID assessmentId) {
        return ResponseEntity.ok(scoreService.getByAssessment(assessmentId));
    }

    // PUT /api/scores/{id}  -> change a score
    @PutMapping("/{id}")
    public ResponseEntity<ScoreDto.Response> update(@PathVariable UUID id,
                                                    @Valid @RequestBody ScoreDto.UpdateRequest request) {
        return ResponseEntity.ok(scoreService.update(id, request));
    }

    // DELETE /api/scores/{id}  -> remove a score
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scoreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
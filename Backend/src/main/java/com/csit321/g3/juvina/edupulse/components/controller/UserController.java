package com.csit321.g3.juvina.edupulse.components.controller;

import com.csit321.g3.juvina.edupulse.components.dto.UserDto;
import com.csit321.g3.juvina.edupulse.components.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CREATE - Create a new user
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto created = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ - Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Get all users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // READ - Get all teachers
    @GetMapping("/teachers")
    public ResponseEntity<List<UserDto>> getAllTeachers() {
        List<UserDto> teachers = userService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    // READ - Get all students
    @GetMapping("/students")
    public ResponseEntity<List<UserDto>> getAllStudents() {
        List<UserDto> students = userService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // READ - Get all admins
    @GetMapping("/admins")
    public ResponseEntity<List<UserDto>> getAllAdmins() {
        List<UserDto> admins = userService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    // UPDATE - Update an existing user
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @Valid @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
package com.csit321.g3.juvina.edupulse.components.service;

import com.csit321.g3.juvina.edupulse.components.dto.UserDto;
import com.csit321.g3.juvina.edupulse.components.entity.User;
import com.csit321.g3.juvina.edupulse.components.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    public UserDto createUser(UserDto userDto) {
        // Check if email already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .isTeacher(userDto.getIsTeacher())
                .isStudent(userDto.getIsStudent())
                .isAdmin(userDto.getIsAdmin())
                .build();

        User saved = userRepository.save(user);
        return mapToDto(saved);
    }

    // READ - Get by ID
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(UUID id) {
        return userRepository.findById(id).map(this::mapToDto);
    }

    // READ - Get by email
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::mapToDto);
    }

    // READ - Get all users
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // READ - Get all teachers
    @Transactional(readOnly = true)
    public List<UserDto> getAllTeachers() {
        return userRepository.findAllTeachers().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // READ - Get all students
    @Transactional(readOnly = true)
    public List<UserDto> getAllStudents() {
        return userRepository.findAllStudents().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // READ - Get all admins
    @Transactional(readOnly = true)
    public List<UserDto> getAllAdmins() {
        return userRepository.findAllAdmins().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // UPDATE
    public Optional<UserDto> updateUser(UUID id, UserDto userDto) {
        return userRepository.findById(id).map(existingUser -> {
            if (userDto.getEmail() != null && !userDto.getEmail().isBlank()
                    && !userDto.getEmail().equalsIgnoreCase(existingUser.getEmail())
                    && userRepository.existsByEmail(userDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
            }

            if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
                existingUser.setEmail(userDto.getEmail());
            }
            if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
                existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
            if (userDto.getFirstName() != null && !userDto.getFirstName().isBlank()) {
                existingUser.setFirstName(userDto.getFirstName());
            }
            if (userDto.getLastName() != null && !userDto.getLastName().isBlank()) {
                existingUser.setLastName(userDto.getLastName());
            }
            if (userDto.getIsTeacher() != null) {
                existingUser.setIsTeacher(userDto.getIsTeacher());
            }
            if (userDto.getIsStudent() != null) {
                existingUser.setIsStudent(userDto.getIsStudent());
            }
            if (userDto.getIsAdmin() != null) {
                existingUser.setIsAdmin(userDto.getIsAdmin());
            }

            return mapToDto(userRepository.save(existingUser));
        });
    }

    // DELETE
    public boolean deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setFullName(user.getFullName());
        dto.setIsTeacher(user.getIsTeacher());
        dto.setIsStudent(user.getIsStudent());
        dto.setIsAdmin(user.getIsAdmin());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
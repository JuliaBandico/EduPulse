package com.csit321.g3.juvina.edupulse.components.service;

import com.csit321.g3.juvina.edupulse.components.dto.ClassDto;
import com.csit321.g3.juvina.edupulse.components.entity.ClassEntity;
import com.csit321.g3.juvina.edupulse.components.entity.User;
import com.csit321.g3.juvina.edupulse.components.repository.ClassRepository;
import com.csit321.g3.juvina.edupulse.components.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;

    public ClassService(ClassRepository classRepository, UserRepository userRepository) {
        this.classRepository = classRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public ClassDto createClass(ClassDto classDto) {
        // Verify teacher exists and has teacher role
        User teacher = userRepository.findById(classDto.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + classDto.getTeacherId()));

        if (!teacher.getIsTeacher()) {
            throw new IllegalArgumentException("User is not a teacher: " + classDto.getTeacherId());
        }

        // Check for duplicate class name for this teacher
        if (classRepository.existsByNameAndTeacherId(classDto.getName(), classDto.getTeacherId())) {
            throw new IllegalArgumentException("Class with name '" + classDto.getName() + "' already exists for this teacher");
        }

        ClassEntity classEntity = ClassEntity.builder()
                .name(classDto.getName())
                .description(classDto.getDescription())
                .subject(classDto.getSubject())
                .section(classDto.getSection())
                .schoolYear(classDto.getSchoolYear())
                .semester(classDto.getSemester())
                .teacher(teacher)
                .build();

        ClassEntity saved = classRepository.save(classEntity);
        return mapToDto(saved);
    }

    // READ - Get by ID
    @Transactional(readOnly = true)
    public Optional<ClassDto> getClassById(UUID id) {
        return classRepository.findById(id).map(this::mapToDto);
    }

    // READ - Get all classes
    @Transactional(readOnly = true)
    public List<ClassDto> getAllClasses() {
        return classRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // READ - Get classes by teacher
    @Transactional(readOnly = true)
    public List<ClassDto> getClassesByTeacher(UUID teacherId) {
        return classRepository.findByTeacherIdOrderByCreatedAtDesc(teacherId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // READ - Get class by ID and teacher (for authorization)
    @Transactional(readOnly = true)
    public Optional<ClassDto> getClassByIdAndTeacher(UUID classId, UUID teacherId) {
        return classRepository.findByIdAndTeacherId(classId, teacherId).map(this::mapToDto);
    }

    // UPDATE
    public Optional<ClassDto> updateClass(UUID id, ClassDto classDto) {
        return classRepository.findById(id).map(existingClass -> {
            UUID teacherIdForValidation = classDto.getTeacherId() != null ? classDto.getTeacherId() : existingClass.getTeacher().getId();
            User teacher = userRepository.findById(teacherIdForValidation)
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + teacherIdForValidation));

            if (!teacher.getIsTeacher()) {
                throw new IllegalArgumentException("User is not a teacher: " + teacherIdForValidation);
            }

            if (classDto.getName() != null && !classDto.getName().isBlank()
                    && !classDto.getName().equals(existingClass.getName())
                    && classRepository.existsByNameAndTeacherId(classDto.getName(), teacherIdForValidation)) {
                throw new IllegalArgumentException("Class with name '" + classDto.getName() + "' already exists for this teacher");
            }

            if (classDto.getName() != null && !classDto.getName().isBlank()) {
                existingClass.setName(classDto.getName());
            }
            if (classDto.getDescription() != null) {
                existingClass.setDescription(classDto.getDescription());
            }
            if (classDto.getSubject() != null) {
                existingClass.setSubject(classDto.getSubject());
            }
            if (classDto.getSection() != null) {
                existingClass.setSection(classDto.getSection());
            }
            if (classDto.getSchoolYear() != null) {
                existingClass.setSchoolYear(classDto.getSchoolYear());
            }
            if (classDto.getSemester() != null) {
                existingClass.setSemester(classDto.getSemester());
            }
            if (classDto.getTeacherId() != null) {
                existingClass.setTeacher(teacher);
            }

            return mapToDto(classRepository.save(existingClass));
        });
    }

    // DELETE
    public boolean deleteClass(UUID id) {
        if (!classRepository.existsById(id)) {
            return false;
        }
        classRepository.deleteById(id);
        return true;
    }

    private ClassDto mapToDto(ClassEntity classEntity) {
        ClassDto dto = new ClassDto();
        dto.setId(classEntity.getId());
        dto.setName(classEntity.getName());
        dto.setDescription(classEntity.getDescription());
        dto.setSubject(classEntity.getSubject());
        dto.setSection(classEntity.getSection());
        dto.setSchoolYear(classEntity.getSchoolYear());
        dto.setSemester(classEntity.getSemester());
        dto.setTeacherId(classEntity.getTeacher().getId());
        dto.setTeacherName(classEntity.getTeacher().getFullName());
        dto.setCreatedAt(classEntity.getCreatedAt());
        dto.setUpdatedAt(classEntity.getUpdatedAt());
        return dto;
    }
}
package com.csit321.g3.juvina.edupulse.service;

import com.csit321.g3.juvina.edupulse.dto.ClassDto;
import com.csit321.g3.juvina.edupulse.entity.ClassEntity;
import com.csit321.g3.juvina.edupulse.entity.User;
import com.csit321.g3.juvina.edupulse.repository.ClassRepository;
import com.csit321.g3.juvina.edupulse.repository.UserRepository;
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
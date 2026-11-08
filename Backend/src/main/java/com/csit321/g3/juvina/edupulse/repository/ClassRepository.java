package com.csit321.g3.juvina.edupulse.repository;

import com.csit321.g3.juvina.edupulse.entity.ClassEntity;
import com.csit321.g3.juvina.edupulse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, UUID> {

    List<ClassEntity> findByTeacher(User teacher);

    List<ClassEntity> findByTeacherId(UUID teacherId);

    Optional<ClassEntity> findByIdAndTeacherId(UUID classId, UUID teacherId);

    @Query("SELECT c FROM ClassEntity c WHERE c.teacher.id = :teacherId ORDER BY c.createdAt DESC")
    List<ClassEntity> findByTeacherIdOrderByCreatedAtDesc(@Param("teacherId") UUID teacherId);

    boolean existsByNameAndTeacherId(String name, UUID teacherId);
}
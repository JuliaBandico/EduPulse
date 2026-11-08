    package com.csit321.g3.juvina.edupulse.components;

import com.csit321.g3.juvina.edupulse.components.dto.AssessmentDto;
import com.csit321.g3.juvina.edupulse.components.dto.ClassDto;
import com.csit321.g3.juvina.edupulse.components.dto.GradeCategoryDto;
import com.csit321.g3.juvina.edupulse.components.dto.UserDto;
import com.csit321.g3.juvina.edupulse.components.service.AssessmentService;
import com.csit321.g3.juvina.edupulse.components.service.ClassService;
import com.csit321.g3.juvina.edupulse.components.service.GradeCategoryService;
import com.csit321.g3.juvina.edupulse.components.service.UserService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CrudContractTest {

    @Test
    void serviceContractsIncludeUpdateAndDeleteMethods() throws NoSuchMethodException {
        assertNotNull(UserService.class.getDeclaredMethod("updateUser", UUID.class, UserDto.class));
        assertNotNull(UserService.class.getDeclaredMethod("deleteUser", UUID.class));

        assertNotNull(ClassService.class.getDeclaredMethod("updateClass", UUID.class, ClassDto.class));
        assertNotNull(ClassService.class.getDeclaredMethod("deleteClass", UUID.class));

        assertNotNull(GradeCategoryService.class.getDeclaredMethod("updateGradeCategory", UUID.class, UUID.class, GradeCategoryDto.class));
        assertNotNull(GradeCategoryService.class.getDeclaredMethod("deleteGradeCategory", UUID.class, UUID.class));

        assertNotNull(AssessmentService.class.getDeclaredMethod("updateAssessment", UUID.class, AssessmentDto.class));
        assertNotNull(AssessmentService.class.getDeclaredMethod("deleteAssessment", UUID.class));
    }
}

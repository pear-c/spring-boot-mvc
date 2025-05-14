package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Student;
import com.nhnacademy.springbootmvc.domain.StudentModifyRequest;
import com.nhnacademy.springbootmvc.exception.StudentNotFoundException;
import com.nhnacademy.springbootmvc.exception.ValidationFailedException;
import com.nhnacademy.springbootmvc.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentController studentController;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("hong", "12345", "홍길동", "gildong@nhnacademy.com", 100, "good");
    }

    @Test
    void getStudent() {
        when(studentRepository.getStudentById("hong")).thenReturn(student);

        Student result = studentController.getStudent("hong");

        assertNotNull(result);
        assertEquals("hong", result.getId());
    }

    @Test
    void testGetStudent_NullInput() {
        Student result = studentController.getStudent(null);
        assertNull(result);
    }

    @Test
    void studentList() {
        Model model = new ExtendedModelMap();
        when(studentRepository.getStudents()).thenReturn(List.of(student));

        String viewName = studentController.studentList((ExtendedModelMap) model);

        assertEquals("students", viewName);
        assertTrue(model.containsAttribute("students"));
    }

    @Test
    void viewStudent() {
        when(studentRepository.getStudentById("hong")).thenReturn(student);
        Model model = new ExtendedModelMap();

        String viewName = studentController.viewStudent("hong", model);

        assertEquals("studentView", viewName);
        assertTrue(model.containsAttribute("student"));
        assertEquals(false, model.getAttribute("hideScore"));
    }

    @Test
    void viewStudentWithoutScore() {
        when(studentRepository.getStudentById("hong")).thenReturn(student);
        Model model = new ExtendedModelMap();

        String viewName = studentController.viewStudentWithoutScore("hong", model);

        assertEquals("studentView", viewName);
        assertTrue(model.containsAttribute("student"));
        assertEquals(true, model.getAttribute("hideScore"));
    }

    @Test
    void testViewStudent_NotFound() {
        when(studentRepository.getStudentById("hong")).thenReturn(null);
        Model model = new ExtendedModelMap();

        assertThrows(StudentNotFoundException.class, () -> studentController.viewStudent("hong", model));
    }

    @Test
    void studentModifyForm() {
        when(studentRepository.getStudentById("hong")).thenReturn(student);

        String viewName = studentController.studentModifyForm("hong");

        assertEquals("studentModify", viewName);
    }

    @Test
    void testStudentModifyForm_NotFound() {
        when(studentRepository.getStudentById("hong")).thenReturn(null);

        assertThrows(StudentNotFoundException.class, () -> studentController.studentModifyForm("hong"));
    }

    @Test
    void modifyStudent() {
        StudentModifyRequest request = new StudentModifyRequest("hong", "12345", "홍길동", "gildong@nhnacademy.com", 100, "good");
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "studentModifyRequest");

        String viewName = studentController.modifyStudent(request, bindingResult);

        verify(studentRepository).registerStudent(any(Student.class));
        assertEquals("redirect:/student/hong", viewName);
    }

    @Test
    void testModifyStudent_ValidationFailed() {
        StudentModifyRequest request = new StudentModifyRequest("", "", "", "", -1, "");
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "studentModifyRequest");
        bindingResult.reject("error");

        assertThrows(ValidationFailedException.class, () -> studentController.modifyStudent(request, bindingResult));
    }
}
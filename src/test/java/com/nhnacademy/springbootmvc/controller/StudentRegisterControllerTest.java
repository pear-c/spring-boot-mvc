package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Student;
import com.nhnacademy.springbootmvc.domain.StudentRegisterRequest;
import com.nhnacademy.springbootmvc.exception.ValidationFailedException;
import com.nhnacademy.springbootmvc.repository.StudentRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentRegisterControllerTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentRegisterController studentRegisterController;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("hong", "12345", "홍길동", "gildong@nhnacademy.com", 100, "good");
    }

    @Test
    void studentRegisterForm() {
        String viewName = studentRegisterController.studentRegisterForm();
        assertEquals("studentRegister", viewName);
    }

    @Test
    void studentRegister_Success() {
        StudentRegisterRequest request = new StudentRegisterRequest("hong", "12345", "홍길동", "gildong@nhnacademy.com", 100, "good");
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "studentRegisterRequest");
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession(true)).thenReturn(session);

        ModelAndView modelAndView = studentRegisterController.studentRegister(request, bindingResult, req, resp);

        verify(studentRepository).registerStudent(any(Student.class));
        verify(session).setAttribute(eq("id"), eq("hong"));
        verify(resp).addCookie(any(Cookie.class));
        assertEquals("redirect:/student/hong", modelAndView.getViewName());
    }

    @Test
    void testStudentRegisterValidationFailed() {
        StudentRegisterRequest request = new StudentRegisterRequest("", "", "", "", -1, "");
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "studentRegisterRequest");
        bindingResult.reject("error");
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        assertThrows(ValidationFailedException.class, () -> studentRegisterController.studentRegister(request, bindingResult, req, resp));
    }
}
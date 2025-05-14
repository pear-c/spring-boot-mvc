package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Student;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentLoginControllerTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentLoginController studentLoginController;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("hong", "12345", "홍길동", "gildong@nhnacademy.com", 100, "good");
    }

    @Test
    void login() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        Model model = new ExtendedModelMap();

        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("id")).thenReturn("hong");
        when(studentRepository.getStudentById("hong")).thenReturn(student);

        String view = studentLoginController.login("SESSION", req, model);

        assertEquals("redirect:/student/hong", view);
    }

    @Test
    void doLogin() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        Model model = new ExtendedModelMap();

        when(studentRepository.matches("hong", "12345")).thenReturn(true);
        when(studentRepository.getStudentById("hong")).thenReturn(student);
        when(req.getSession(true)).thenReturn(session);

        String view = studentLoginController.doLogin("hong", "12345", req, resp, model);

        Mockito.verify(session).setAttribute("id", "hong");
        Mockito.verify(resp).addCookie(any(Cookie.class));
        assertEquals("redirect:/student/hong", view);
    }
}
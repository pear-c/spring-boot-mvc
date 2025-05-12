package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Student;
import com.nhnacademy.springbootmvc.repository.StudentRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
@RequestMapping("/student/register")
public class StudentRegisterController {
    private final StudentRepository studentRepository;

    @GetMapping
    public String studentRegisterForm() {
        return "studentRegister";
    }

    @PostMapping
    public ModelAndView studentRegister(@ModelAttribute("student") Student student,
                                        HttpServletRequest req,
                                        HttpServletResponse resp) {
        studentRepository.registerStudent(student);

        HttpSession session = req.getSession(true);
        session.setAttribute("id", student.getId());

        Cookie cookie = new Cookie("SESSION", session.getId());
        resp.addCookie(cookie);

        return new ModelAndView("redirect:/student/" + student.getId());
    }
}

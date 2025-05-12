package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Student;
import com.nhnacademy.springbootmvc.repository.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;

    @ModelAttribute("student")
    public Student getStudent(@PathVariable(value = "studentId", required = false) String studentId) {
        if(studentId != null) {
            return studentRepository.getStudentById(studentId);
        }
        return new Student();
    }

    @GetMapping
    public String studentList(@CookieValue(value = "SESSION", required = false) String sessionId,
                              HttpServletRequest req,
                              ModelMap model) {
        if(!isLoggedIn(sessionId, req)) {
            return "redirect:/login";
        }
        model.put("students", studentRepository.getStudents());
        return "students";
    }

    @GetMapping("/{studentId}")
    public String viewStudent(@PathVariable String studentId,
                              @CookieValue(value = "SESSION", required = false) String sessionId,
                              HttpServletRequest req, Model model,
                              @ModelAttribute("student") Student student) {
        if(!isLoggedIn(sessionId, req)) {
            return "redirect:/login";
        }

        Student masked = Student.constructPasswordMaskedStudent(student);
        model.addAttribute("student", masked);
        model.addAttribute("hideScore", false);
        return "studentView";
    }

    @GetMapping(value = "/{studentId}", params = "hideScore=yes")
    public String viewStudentWithoutScore(@PathVariable("studentId") String studentId,
                                          @CookieValue(value = "SESSION", required = false) String sessionId,
                                          HttpServletRequest req,
                                          Model model) {

        if (!isLoggedIn(sessionId, req)) {
            return "redirect:/login";
        }

        Student student = studentRepository.getStudentById(studentId);
        Student masked = Student.constructPasswordMaskedStudent(student);
        model.addAttribute("student", masked);
        model.addAttribute("hideScore", true);
        return "studentView";
    }

    @GetMapping("/{studentId}/modify")
    public String studentModifyForm(@CookieValue(value = "SESSION", required = false) String sessionId,
                                    HttpServletRequest req) {
        if (!isLoggedIn(sessionId, req)) {
            return "redirect:/login";
        }

        return "studentModify";
    }

    @PostMapping("/{studentId}/modify")
    public String modifyStudent(@CookieValue(value = "SESSION", required = false) String sessionId,
                                HttpServletRequest req,
                                @ModelAttribute("student") Student student) {

        if (!isLoggedIn(sessionId, req)) {
            return "redirect:/login";
        }

        studentRepository.registerStudent(student);
        return "redirect:/student/" + student.getId();
    }

    private boolean isLoggedIn(String sessionId, HttpServletRequest request) {
        if (!StringUtils.hasText(sessionId)) {
            return false;
        }
        HttpSession session = request.getSession(false);
        return session != null && StringUtils.hasText((String) session.getAttribute("id"));
    }
}

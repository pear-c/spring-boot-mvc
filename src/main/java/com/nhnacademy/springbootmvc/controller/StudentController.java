package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Student;
import com.nhnacademy.springbootmvc.domain.StudentModifyRequest;
import com.nhnacademy.springbootmvc.exception.StudentNotFoundException;
import com.nhnacademy.springbootmvc.exception.ValidationFailedException;
import com.nhnacademy.springbootmvc.repository.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;

    @ModelAttribute("student")
    public Student getStudent(@PathVariable(value = "studentId", required = false) String studentId) {
        Student student = studentRepository.getStudentById(studentId);
        return student;
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
                              HttpServletRequest req, Model model) {
        if(!isLoggedIn(sessionId, req)) {
            return "redirect:/login";
        }

        Student student = studentRepository.getStudentById(studentId);
        if(student == null) {
            throw new StudentNotFoundException();
        }

        Student masked = Student.constructPasswordMaskedStudent(student);
        model.addAttribute("student", masked);
        model.addAttribute("hideScore", false);
        return "studentView";
    }

    @GetMapping(value = "/{studentId}", params = "hideScore=yes")
    public String viewStudentWithoutScore(@PathVariable String studentId,
                                          @CookieValue(value = "SESSION", required = false) String sessionId,
                                          HttpServletRequest req, Model model) {

        if (!isLoggedIn(sessionId, req)) {
            return "redirect:/login";
        }

        Student student = studentRepository.getStudentById(studentId);
        if(student == null) {
            throw new StudentNotFoundException();
        }

        Student masked = Student.constructPasswordMaskedStudent(student);
        model.addAttribute("student", masked);
        model.addAttribute("hideScore", true);
        return "studentView";
    }

    @GetMapping("/{studentId}/modify")
    public String studentModifyForm(@PathVariable String studentId,
                                    @CookieValue(value = "SESSION", required = false) String sessionId,
                                    HttpServletRequest req) {
        if (!isLoggedIn(sessionId, req)) {
            return "redirect:/login";
        }

        Student student = studentRepository.getStudentById(studentId);
        if(student == null) {
            throw new StudentNotFoundException();
        }

        return "studentModify";
    }

    @PostMapping("/{studentId}/modify")
    public String modifyStudent(@CookieValue(value = "SESSION", required = false) String sessionId,
                                HttpServletRequest req,
                                @Valid @ModelAttribute StudentModifyRequest studentModifyRequest,
                                BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        if (!isLoggedIn(sessionId, req)) {
            return "redirect:/login";
        }

        Student student = Student.createStudent(
                studentModifyRequest.getId(),
                studentModifyRequest.getPassword(),
                studentModifyRequest.getName(),
                studentModifyRequest.getEmail(),
                studentModifyRequest.getScore(),
                studentModifyRequest.getComment());

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

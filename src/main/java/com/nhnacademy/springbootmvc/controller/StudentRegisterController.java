package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Student;
import com.nhnacademy.springbootmvc.domain.StudentRegisterRequest;
import com.nhnacademy.springbootmvc.exception.ValidationFailedException;
import com.nhnacademy.springbootmvc.repository.StudentRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    public ModelAndView studentRegister(@Valid @ModelAttribute StudentRegisterRequest studentRegisterRequest,
                                        BindingResult bindingResult,
                                        HttpServletRequest req,
                                        HttpServletResponse resp) {

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        Student student = Student.createStudent(
                studentRegisterRequest.getId(),
                studentRegisterRequest.getPassword(),
                studentRegisterRequest.getName(),
                studentRegisterRequest.getEmail(),
                studentRegisterRequest.getScore(),
                studentRegisterRequest.getComment());

        studentRepository.registerStudent(student);

        HttpSession session = req.getSession(true);
        session.setAttribute("id", student.getId());

        Cookie cookie = new Cookie("SESSION", session.getId());
        resp.addCookie(cookie);

        return new ModelAndView("redirect:/student/" + student.getId());
    }
}

package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;

    @GetMapping
    public String studentList(ModelMap model) {
        model.put("students", studentRepository.getStudents());
        return "students";
    }

    @GetMapping("/{studentId}")
    public String viewStudent() {
        return "studentView";
    }
}

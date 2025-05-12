package com.nhnacademy.springbootmvc.controller;


import com.nhnacademy.springbootmvc.domain.Student;
import com.nhnacademy.springbootmvc.repository.StudentRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class StudentLoginController {

    private final StudentRepository studentRepository;

    @GetMapping("/login")
    public String login(@CookieValue(value = "SESSION", required = false) String sessionId,
                        HttpServletRequest req,
                        Model model) {
        if(StringUtils.hasText(sessionId)) {
            HttpSession session = req.getSession(false);
            if(session != null) {
                String studnetId = (String) session.getAttribute("id");
                if(StringUtils.hasText(studnetId)) {
                    Student student = studentRepository.getStudentById(studnetId);
                    Student masked = Student.constructPasswordMaskedStudent(student);
                    model.addAttribute("student", masked);
                    return "redirect:/student/" + masked.getId();
                }
            }
        }
        return "loginForm";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("id") String id, @RequestParam("pwd") String pwd,
                          HttpServletRequest req, HttpServletResponse resp,
                          Model model) {
        if(studentRepository.matches(id, pwd)) {
            HttpSession session = req.getSession(true);
            session.setAttribute("id", id);

            Cookie cookie = new Cookie("SESSION", session.getId());
            resp.addCookie(cookie);

            Student student = studentRepository.getStudentById(id);
            Student maskedStudent = Student.constructPasswordMaskedStudent(student);
            model.addAttribute("student", maskedStudent);

            return "redirect:/student/" + id;
        }
        return "redirect:/login";
    }
}

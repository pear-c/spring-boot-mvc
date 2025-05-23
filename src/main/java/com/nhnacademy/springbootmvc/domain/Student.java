package com.nhnacademy.springbootmvc.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Student {
    private static final String MASK = "*****";

    private String id;
    private String password;
    private String name;
    private String email;
    private int score;
    private String comment;

    public static Student createStudent(String id, String password, String name, String email, int score, String comment) {
        return new Student(id, password, name, email, score, comment);
    }

    public static Student constructPasswordMaskedStudent(Student student) {
        Student newStudent = student.createStudent(student.getId(), MASK, student.getName(), student.getEmail(), student.getScore(), student.getComment());
        return newStudent;
    }
}

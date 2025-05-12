package com.nhnacademy.springbootmvc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Student {
    private String id;
    private String password;
    private String name;
    private String email;
    private int score;
    private String comment;

    public static Student createStudent(String id, String password, String name, String email, int score, String comment) {
        return new Student(id, password, name, email, score, comment);
    }

    private static final String MASK = "*****";

    public static Student constructPasswordMaskedStudent(Student student) {
        Student newStudnet = student.createStudent(student.getId(), MASK, student.getName(), student.getEmail(), student.getScore(), student.getComment());
        return newStudnet;
    }
}

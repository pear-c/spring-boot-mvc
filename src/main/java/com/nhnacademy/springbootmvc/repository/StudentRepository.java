package com.nhnacademy.springbootmvc.repository;

import com.nhnacademy.springbootmvc.domain.Student;

import java.util.List;

public interface StudentRepository {
    boolean existsById(String id);
    boolean matches(String id, String password);
    Student getStudentById(String id);
    void registerStudent(Student student);
    List<Student> getStudents();
}

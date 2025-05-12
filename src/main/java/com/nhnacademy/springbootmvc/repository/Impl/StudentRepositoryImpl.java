package com.nhnacademy.springbootmvc.repository.Impl;

import com.nhnacademy.springbootmvc.domain.Student;
import com.nhnacademy.springbootmvc.repository.StudentRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class StudentRepositoryImpl implements StudentRepository {
    private final Map<String, Student> studentMap = new HashMap<>();

    public StudentRepositoryImpl() {
        studentMap.put("hong", Student.createStudent("hong", "12345", "홍길동","gildong@nhnacademy.com", 100, "good"));
    }

    @Override
    public boolean existsById(String id) {
        return studentMap.containsKey(id);
    }

    @Override
    public boolean matches(String id, String password) {
        return Optional.ofNullable(getStudentById(id))
                .map(student -> student.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    public Student getStudentById(String id) {
        return studentMap.get(id);
    }

    @Override
    public void registerStudent(Student student) {
        studentMap.put(student.getId(), student);
    }

    @Override
    public List<Student> getStudents() {
        return studentMap.values().stream().toList();
    }
}

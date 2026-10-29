package com.ablekimov.studentapi.repository;

import com.ablekimov.studentapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Feature by Ablekimov Alisher: search students by (part of) last name, case-insensitive
    List<Student> findByLastNameContainingIgnoreCase(String lastName);
}

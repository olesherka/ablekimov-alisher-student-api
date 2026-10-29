package com.ablekimov.studentapi.controller;

import com.ablekimov.studentapi.model.Student;
import com.ablekimov.studentapi.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // READ all
    @GetMapping
    public List<Student> getAll() {
        return service.findAll();
    }

    // READ (improved by Ablekimov Alisher): GET /api/students/search?lastName=able
    @GetMapping("/search")
    public List<Student> search(@RequestParam("lastName") String lastName) {
        return service.searchByLastName(lastName);
    }

    // READ one
    @GetMapping("/{id}")
    public Student getById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Student> create(@Valid @RequestBody Student student) {
        Student saved = service.create(student);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Student update(@PathVariable("id") Long id, @Valid @RequestBody Student student) {
        return service.update(id, student);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.ablekimov.studentapi;

import com.ablekimov.studentapi.model.Student;
import com.ablekimov.studentapi.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the search feature added on branch feature/ablekimov-alisher-crud.
 */
@SpringBootTest
@AutoConfigureMockMvc
class StudentSearchTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.save(new Student("Alisher", "Ablekimov", "alisher.ablekimov.37637@example.com", "IT2-2312"));
        repository.save(new Student("Test", "Student", "test.student.37637@example.com", "IT2-2312"));
    }

    @Test
    void searchFindsStudentByPartOfLastName() throws Exception {
        mockMvc.perform(get("/api/students/search").param("lastName", "ablek"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Alisher"));
    }

    @Test
    void searchReturnsEmptyListWhenNothingMatches() throws Exception {
        mockMvc.perform(get("/api/students/search").param("lastName", "nobody"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}

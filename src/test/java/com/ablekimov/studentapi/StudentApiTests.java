package com.ablekimov.studentapi;

import com.ablekimov.studentapi.repository.StudentRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Automated CRUD tests for the Student API (Ablekimov Alisher, ID 37637).
 * MockMvc calls the controllers directly, so URLs here are without the context path.
 */
@SpringBootTest
@AutoConfigureMockMvc
class StudentApiTests {

    private static final String ALISHER_JSON = """
            {"firstName":"Alisher","lastName":"Ablekimov",
             "email":"alisher.ablekimov.37637@example.com","groupName":"IT2-2312"}
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    private long createAlisher() throws Exception {
        String response = mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ALISHER_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Number id = JsonPath.read(response, "$.id");
        return id.longValue();
    }

    @Test
    void homePageShowsStudentName() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Ablekimov Alisher"))
                .andExpect(jsonPath("$.studentId").value("37637"))
                .andExpect(jsonPath("$.group").value("IT2-2312"));
    }

    @Test
    void createStudent() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ALISHER_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value("Alisher"))
                .andExpect(jsonPath("$.lastName").value("Ablekimov"));
    }

    @Test
    void getAllStudents() throws Exception {
        createAlisher();
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].lastName").value("Ablekimov"));
    }

    @Test
    void getStudentById() throws Exception {
        long id = createAlisher();
        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.email").value("alisher.ablekimov.37637@example.com"));
    }

    @Test
    void updateStudent() throws Exception {
        long id = createAlisher();
        String updated = """
                {"firstName":"Alisher","lastName":"Ablekimov",
                 "email":"alisher.updated.37637@example.com","groupName":"IT2-2312"}
                """;
        mockMvc.perform(put("/api/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alisher.updated.37637@example.com"));
    }

    @Test
    void deleteStudent() throws Exception {
        long id = createAlisher();
        mockMvc.perform(delete("/api/students/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void nonexistentStudentReturns404() throws Exception {
        mockMvc.perform(get("/api/students/{id}", 99999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student with id 99999 exists"));
    }

    @Test
    void invalidStudentReturns400() throws Exception {
        String invalid = """
                {"firstName":"","lastName":"Ablekimov","email":"not-an-email","groupName":"IT2-2312"}
                """;
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.firstName").exists())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }
}

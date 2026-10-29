package com.ablekimov.studentapi.controller;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Home endpoint: shows who made this application and which version is running.
 */
@RestController
public class InfoController {

    private final ObjectProvider<BuildProperties> buildProperties;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${student.full-name}")
    private String studentName;

    @Value("${student.id}")
    private String studentId;

    @Value("${student.group}")
    private String studentGroup;

    @Value("${student.github}")
    private String github;

    public InfoController(ObjectProvider<BuildProperties> buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/")
    public Map<String, Object> info() {
        BuildProperties build = buildProperties.getIfAvailable();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("application", applicationName);
        body.put("author", studentName);
        body.put("studentId", studentId);
        body.put("group", studentGroup);
        body.put("github", github);
        body.put("version", build != null ? build.getVersion() : "unknown");
        body.put("serverTime", LocalDateTime.now().toString());
        body.put("endpoints", List.of(
                "GET    /api/students",
                "GET    /api/students/{id}",
                "POST   /api/students",
                "PUT    /api/students/{id}",
                "DELETE /api/students/{id}"));
        return body;
    }
}

package com.ablekimov.studentapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

/**
 * Prints the student's name into the application log on startup,
 * so it is visible in "docker logs" and in the Jenkins console.
 */
@Component
public class StartupLogger implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupLogger.class);

    private final ObjectProvider<BuildProperties> buildProperties;

    @Value("${student.full-name}")
    private String studentName;

    @Value("${student.id}")
    private String studentId;

    @Value("${student.group}")
    private String studentGroup;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    public StartupLogger(ObjectProvider<BuildProperties> buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Override
    public void run(ApplicationArguments args) {
        BuildProperties build = buildProperties.getIfAvailable();
        String version = build != null ? build.getVersion() : "unknown";
        log.info("==========================================================");
        log.info(" Student API by {} (ID {}, group {})", studentName, studentId, studentGroup);
        log.info(" Version: {}", version);
        log.info(" API base path: {}/api/students", contextPath);
        log.info("==========================================================");
    }
}

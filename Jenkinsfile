// Jenkins pipeline for the Student API
// Author: Ablekimov Alisher (ID 37637, group IT2-2312)
// Job name in Jenkins: Ablekimov_Alisher_Student_API

pipeline {
    agent any

    tools {
        // JDK configured in Manage Jenkins -> Tools (name must match)
        jdk 'jdk21'
    }

    triggers {
        // Check GitHub for new commits every ~2 minutes
        pollSCM('H/2 * * * *')
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '20'))
        disableConcurrentBuilds()
    }

    environment {
        STUDENT_NAME = 'Ablekimov Alisher'
        STUDENT_ID   = '37637'
        IMAGE_NAME   = 'ablekimov-alisher-student-api'
        IMAGE_TAG    = 'v1.0-ablekimov-alisher'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "=== Pipeline started by ${env.STUDENT_NAME} (ID ${env.STUDENT_ID}) ==="
                checkout scm
                sh 'git log -1 --pretty=format:"Last commit: %h %s (%an)"'
            }
        }

        stage('Build') {
            steps {
                echo "=== Build: compiling the Spring Boot app of ${env.STUDENT_NAME} ==="
                sh 'chmod +x gradlew'
                sh 'java -version'
                sh './gradlew --no-daemon clean bootJar -x test'
            }
        }

        stage('Test') {
            steps {
                echo "=== Test: running automated tests for ${env.STUDENT_NAME} ==="
                // If any test fails, this step fails and the next stages are skipped
                sh './gradlew --no-daemon test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "=== Docker Build: ${env.IMAGE_NAME}:${env.IMAGE_TAG} ==="
                sh '''
                    docker build \
                      --build-arg APP_TAG=${IMAGE_TAG} \
                      -t ${IMAGE_NAME}:${IMAGE_TAG} \
                      -t ${IMAGE_NAME}:build-${BUILD_NUMBER} \
                      .
                '''
            }
        }

        stage('Verify') {
            steps {
                echo "=== Verify: checking that the image of ${env.STUDENT_NAME} exists ==="
                sh '''
                    docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} \
                      --format "Image: {{index .RepoTags 0}} | Created: {{.Created}} | Size: {{.Size}} bytes"
                    docker images ${IMAGE_NAME}
                '''
            }
        }
    }

    post {
        success {
            echo "SUCCESS: image ${env.IMAGE_NAME}:${env.IMAGE_TAG} built for ${env.STUDENT_NAME}"
        }
        failure {
            echo "FAILURE: pipeline of ${env.STUDENT_NAME} failed - check the stage marked red above"
        }
    }
}

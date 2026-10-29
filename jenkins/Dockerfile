# Local Jenkins with Java 21 and Docker CLI
# Author: Ablekimov Alisher (ID 37637, group IT2-2312)
FROM jenkins/jenkins:lts-jdk21

USER root

# Docker CLI (+ buildx plugin) so pipelines can run "docker build".
# The Docker engine itself is Docker Desktop on the host, reached through /var/run/docker.sock
COPY --from=docker:27-cli /usr/local/bin/docker /usr/local/bin/docker
COPY --from=docker:27-cli /usr/local/libexec/docker/cli-plugins/docker-buildx /usr/local/libexec/docker/cli-plugins/docker-buildx

# Git is already included in the Jenkins image

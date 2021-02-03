## Dockerfile from /resources/docker
FROM openjdk:13-jdk-alpine as jlink-package

RUN jlink \
     --verbose \
     --strip-java-debug-attributes \
     --compress=2 \
     --no-man-pages \
     --no-header-files \
     --module-path /opt/openjdk-13/jmods \
     --add-modules jdk.jfr,jdk.management.agent,java.base,java.sql,java.logging,java.xml,jdk.unsupported,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
     --output /opt/jdk-13-mini-runtime


FROM frolvlad/alpine-glibc:alpine-3.12_glibc-2.32

ENV JAVA_HOME=/opt/jdk-13-mini-runtime
ENV PATH="$PATH:$JAVA_HOME/bin"
ENV LANG=C.UTF-8

COPY --from=jlink-package /opt/jdk-13-mini-runtime /opt/jdk-13-mini-runtime

RUN mkdir /app
ARG JAR_FILE
COPY target/${JAR_FILE} /app/application.jar
ADD https://github.com/lightstep/otel-launcher-java/releases/download/0.14.0/lightstep-opentelemetry-javaagent-0.14.0.jar /app/lightstep-opentelemetry-javaagent-0.14.0.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/application.jar"]
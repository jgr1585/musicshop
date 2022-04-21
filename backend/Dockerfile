FROM openjdk:14-jdk-alpine
ARG JAR_FILE=**/*SNAPSHOT.jar
COPY ${JAR_FILE} backend.jar
ENTRYPOINT ["java","-jar","backend.jar"]
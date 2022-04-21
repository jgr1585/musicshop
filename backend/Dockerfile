FROM openjdk:14-jdk-alpine
ARG JAR_FILE=backend/build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} musicshop-backend.jar
ENTRYPOINT ["java","-jar","musicshop-backend.jar"]
FROM openjdk:14-jdk-alpine
ARG JAR_FILE=**/*SNAPSHOT.jar
COPY ${JAR_FILE} client-customerdb.jar
ENTRYPOINT ["java","-jar","client-customerdb.jar"]
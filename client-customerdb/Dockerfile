FROM openjdk:14-jdk-alpine
ARG JAR_FILE=client-customeredb/build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} musicshop-client-customerdb.jar
ENTRYPOINT ["java","-jar","musicshop-client-customerdb.jar"]
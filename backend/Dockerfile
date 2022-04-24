FROM openjdk:11
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.rmi.server.hostname=10.0.40.166", "-jar", "app.jar"]
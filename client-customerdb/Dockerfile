FROM openjdk:11
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-DDB_URL='jdbc:postgresql://localhost:5432/postgres'", "-DDB_USER=postgres", "-DDB_PASS=f-x_NzzB4E", "-DDB_CUSTOMERS_TABLE=customers", "-DDB_DRIVER_FULLY_QUALIFIED_CLASSNAME=org.postgresql.Driver", "-DLIST_MAX_RESULTS=50", "-jar", "app.jar"]
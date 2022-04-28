FROM openjdk:11
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.rmi.server.hostname=10.0.40.167", "-DDB_URL=jdbc:postgresql://localhost:5432/postgres", "-DDB_USER=postgres", "-DDB_PASS=f-x_NzzB4E", "-DDB_CUSTOMERS_TABLE=customers", "-DDB_DRIVER_FULLY_QUALIFIED_CLASSNAME=org.postgresql.Driver", "-DLIST_MAX_RESULTS=50", "-jar", "app.jar"]

FROM quay.io/wildfly/wildfly
RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#007 --silent
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
ADD customerdb-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
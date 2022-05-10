FROM quay.io/wildfly/wildfly
RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#007 --silent
RUN /opt/jboss/wildfly/bin/add-user.sh -a -u backend -p backend --silent
ADD build/libs/backend-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
ADD https://jdbc.postgresql.org/download/postgresql-42.3.3.jar /opt/jboss/wildfly/standalone/deployments/postgresql-42.3.3.jar.dodeploy
RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history
RUN rm /opt/jboss/wildfly/standalone/configuration/standalone-full.xml
COPY standalone-full.xml /opt/jboss/wildfly/standalone/configuration/
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-full.xml"]
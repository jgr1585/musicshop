FROM quay.io/wildfly/wildfly
RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#007 --silent
RUN /opt/jboss/wildfly/bin/add-user.sh -a -u backend -p backend --silent
ADD build/libs/backend-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
COPY VM/config.cli /opt/jboss/wildfly/bin/
ADD https://jdbc.postgresql.org/download/postgresql-42.3.3.jar /opt/jboss/wildfly/standalone/deployments/postgresql-42.3.3.jar.dodeploy
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file="/opt/jboss/wildfly/bin/config.cli"
RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history
RUN rm /opt/jboss/wildfly/standalone/configuration/standalone-full.xml
COPY VM/standalone-full.xml /opt/jboss/wildfly/standalone/configuration/
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-full.xml"]
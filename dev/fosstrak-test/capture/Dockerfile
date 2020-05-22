FROM tomcat:7.0

# Get EPCIS repository image
ADD https://oss.sonatype.org/content/repositories/public/org/fosstrak/capturingapp/capturingapp/0.1.1/capturingapp-0.1.1.war /tmp/capturingapp-0.1.1.war

WORKDIR /tmp

# Extract downloaded file and unpack the .war to the Tomcat webapps directory
RUN unzip capturingapp-0.1.1.war -d ${CATALINA_HOME}/webapps/capturingapp-0.1.1

# Confgure capturing application URL
ADD captureapplication.properties ${CATALINA_HOME}/webapps/capturingapp-0.1.1/WEB-INF/classes/captureapplication.properties

# Confgure capturing application changeset
ADD changeset.xml ${CATALINA_HOME}/webapps/capturingapp-0.1.1/WEB-INF/classes/changeset.xml

# Run the server
ENTRYPOINT ["catalina.sh", "run"]
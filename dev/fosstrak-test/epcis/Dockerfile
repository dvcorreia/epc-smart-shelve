FROM tomcat:7.0


# Get EPCIS repository image
ADD https://oss.sonatype.org/content/repositories/public/org/fosstrak/epcis/epcis-repository/0.5.0/epcis-repository-0.5.0-bin-with-dependencies.zip /tmp/epcis-repository.zip

# Get MySQL ConnectorJ
ADD http://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.33.tar.gz /tmp/mysql-connector.tar.gz

WORKDIR /tmp

# Copy EPIC repository war file to tomcat dir
RUN unzip epcis-repository.zip && \
    rm epcis-repository.zip && \
    mv epcis-repository-* epcis-repository && \
    unzip epcis-repository/*.war -d ${CATALINA_HOME}/webapps/epcis-repository-0.5.0 && \
    rm -r epcis-repository

# Extract MySQL ConnectorJ
RUN tar -zxvf mysql-connector.tar.gz && \
    rm mysql-connector.tar.gz && \
    mv mysql-connector-java-* mysql-connector-java && \
    cp mysql-connector-java/*.jar ${CATALINA_HOME}/lib/mysql-connector-java-5.1.33-bin.jar

# Add repository configuration to connect to the database
# COPY log4j.xml ${CATALINA_HOME}/webapps/epcis-repository-0.5.0/WEB-INF/classes/log4j.xml
ADD epcis-repository-0.5.0.xml ${CATALINA_HOME}/conf/Catalina/localhost/epcis-repository-0.5.0.xml
ADD epcis-repository-0.5.0.xml ${CATALINA_HOME}/webapps/epcis-repository-0.5.0/META-INF/context.xml

# Run the server
ENTRYPOINT ["catalina.sh", "run"]
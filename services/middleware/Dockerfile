FROM tomcat:7.0

LABEL maintainer="dv.correia@ua.pt"

# Get ALE F&C server
ADD https://oss.sonatype.org/content/repositories/public/org/fosstrak/fc/fc-server/1.2.0/fc-server-1.2.0-bin-with-dependencies.zip /tmp/fc-server.zip

# Get ALE F&C web client
ADD https://oss.sonatype.org/content/repositories/public/org/fosstrak/fc/fc-webclient/1.2.0/fc-webclient-1.2.0-bin-with-dependencies.zip /tmp/fc-webclient.zip

WORKDIR /tmp

# Copy ALE F&C server war file to tomcat dir
RUN unzip fc-server.zip && \
    rm fc-server.zip && \
    mv fc-server-* fc-server && \
    unzip fc-server/*.war -d ${CATALINA_HOME}/webapps/fc-server-1.2.0 && \
    rm -r fc-server

# Copy ALE F&C web client war file to tomcat dir
RUN unzip fc-webclient.zip && \
    rm fc-webclient.zip && \
    mv fc-webclient-* fc-webclient && \
    unzip fc-webclient/*.war -d ${CATALINA_HOME}/webapps/fc-webclient-1.2.0 && \
    rm -r fc-webclient

# Add configuration files for ALE F&C server
COPY log4j.xml ${CATALINA_HOME}/webapps/fc-server-1.2.0/WEB-INF/classes/log4j.xml
COPY InputGenerators.properties ${CATALINA_HOME}/webapps/fc-server-1.2.0/WEB-INF/classes/InputGenerators.properties
COPY LLRPReader.xml ${CATALINA_HOME}/webapps/fc-server-1.2.0/WEB-INF/classes/LLRPReader.xml

EXPOSE 8080

ENTRYPOINT ["catalina.sh", "run"]
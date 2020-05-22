FROM tomcat:6.0


# Get EPCIS repository image
ADD https://oss.sonatype.org/content/repositories/public/org/fosstrak/webadapters/epcis-webadapter/0.1.0/epcis-webadapter-0.1.0-bin-with-dependencies.zip  /tmp/epcis-webadapter.zip

WORKDIR /tmp

# Copy EPIC repository war file to tomcat dir
RUN unzip epcis-webadapter.zip && \
    rm epcis-webadapter.zip && \
    mv epcis-webadapter-* epcis-webadapter && \
    mv epcis-webadapter/*.war ${CATALINA_HOME}/webapps/epcis-webadapter-0.1.0.war

# Run the server
ENTRYPOINT ["catalina.sh", "run"]
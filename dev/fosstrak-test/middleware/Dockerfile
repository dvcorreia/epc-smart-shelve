FROM tomcat:7.0

# Get ALE F&C server
ADD https://oss.sonatype.org/content/repositories/public/org/fosstrak/fc/fc-server/1.2.0/fc-server-1.2.0-bin-with-dependencies.zip /tmp/fc-server.zip

# Get ALE F&C web client
ADD https://oss.sonatype.org/content/repositories/public/org/fosstrak/fc/fc-webclient/1.2.0/fc-webclient-1.2.0-bin-with-dependencies.zip /tmp/fc-webclient.zip

WORKDIR /tmp

# Copy ALE F&C server war file to tomcat dir
RUN unzip fc-server.zip && \
    mv fc-server-*/*.war /usr/local/tomcat/webapps && \
    rm -rf fc-server-* fc-server.zip

# Copy ALE F&C web client war file to tomcat dir
RUN unzip fc-webclient.zip && \
    mv fc-webclient-*/*.war /usr/local/tomcat/webapps && \
    rm -rf fc-webclient-* fc-webclient.zip

CMD ["catalina.sh", "run"]
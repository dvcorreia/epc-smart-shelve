FROM tomcat:7.0

LABEL maintainer="dv.correia@ua.pt"

# Get ALE F&C web client
ADD https://oss.sonatype.org/content/repositories/public/org/fosstrak/fc/fc-webclient/1.2.0/fc-webclient-1.2.0-bin-with-dependencies.zip /tmp/fc-webclient.zip

WORKDIR /tmp

# Copy ALE F&C web client war file to tomcat dir
RUN unzip fc-webclient.zip && \
    rm fc-webclient.zip && \
    mv fc-webclient-* fc-webclient && \
    mv fc-webclient/*.war /usr/local/tomcat/webapps/fc-webclient-1.2.0.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
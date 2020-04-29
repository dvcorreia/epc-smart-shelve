FROM tomcat:7.0

LABEL maintainer="dv.correia@ua.pt"

# Get ALE F&C server
ADD https://oss.sonatype.org/content/repositories/public/org/fosstrak/fc/fc-server/1.2.0/fc-server-1.2.0-bin-with-dependencies.zip /tmp/fc-server.zip

WORKDIR /tmp

# Copy ALE F&C server war file to tomcat dir
RUN unzip fc-server.zip && \
    rm fc-server.zip && \
    mv fc-server-* fc-server && \
    unzip fc-server/*.war -d /usr/local/tomcat/webapps/fc-server-1.2.0 && \
    rm -r fc-server

# Add configuration files for ALE F&C server
ADD log4j.xml ${CATALINA_BASE}/webapps/fc-server-1.2.0/WEB-INF/classes/log4j.xml

EXPOSE 8080

CMD ["catalina.sh", "run"]
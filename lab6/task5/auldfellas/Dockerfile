FROM openjdk:8-jre-alpine
ENV HOSTNAME=localhost
ENV BROKER=broker
COPY target/auldfellas-0.0.1-allinone.jar /auldfellas-0.0.1.jar
CMD /usr/bin/java -jar /auldfellas-0.0.1.jar $HOSTNAME $BROKER
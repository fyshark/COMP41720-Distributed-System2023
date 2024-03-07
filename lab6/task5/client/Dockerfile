FROM openjdk:8-jre-alpine
ENV HOSTNAME=localhost
ENV BROKER=broker
COPY target/client-0.0.1-allinone.jar /client-0.0.1.jar
CMD /usr/bin/java -jar /client-0.0.1.jar $HOSTNAME $BROKER
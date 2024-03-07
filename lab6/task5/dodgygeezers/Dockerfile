FROM openjdk:8-jre-alpine
ENV HOSTNAME=localhost
ENV BROKER=broker
COPY target/dodgygeezers-0.0.1-allinone.jar /dodgygeezers-0.0.1.jar
CMD /usr/bin/java -jar /dodgygeezers-0.0.1.jar $HOSTNAME $BROKER
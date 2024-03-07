FROM openjdk:8-jre-alpine
ENV HOSTNAME=localhost
ENV BROKER=broker
COPY target/girlsallowed-0.0.1-allinone.jar /girlsallowed-0.0.1.jar
CMD /usr/bin/java -jar /girlsallowed-0.0.1.jar $HOSTNAME $BROKER
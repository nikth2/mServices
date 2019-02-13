FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=./target/mservices-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

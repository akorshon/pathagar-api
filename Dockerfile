#FROM adoptopenjdk/openjdk11-openj9
FROM openjdk:17-alpine
COPY build/libs/pathagar-api*.jar /opt/pathagar/app.jar
WORKDIR /opt/pathagar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]


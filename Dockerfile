#FROM  eclipse-temurin:21-jdk
FROM  amazoncorretto:21-alpine-jdk
COPY build/libs/pathagar-api*.jar /opt/pathagar/app.jar
WORKDIR /opt/pathagar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]


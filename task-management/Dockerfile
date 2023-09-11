FROM openjdk:17-jdk-slim

ENV TZ="America/Sao_Paulo"

WORKDIR /opt/api

COPY /target/api.jar /opt/api/api.jar

CMD java -jar api.jar
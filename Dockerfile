FROM openjdk:17.0.2-jdk-windowsservercore-ltsc2022

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM mcr.microsoft.com/openjdk/jdk:17-windowsservercore-ltsc2022

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

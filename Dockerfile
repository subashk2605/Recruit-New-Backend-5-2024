FROM openjdk:17-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV MYSQL_HOST=localhost
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=recruit_app
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=343592
ENTRYPOINT ["java", "-jar", "/app.jar"]

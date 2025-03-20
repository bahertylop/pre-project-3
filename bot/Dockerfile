FROM openjdk:20-jdk-alpine
COPY target/*.jar /application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
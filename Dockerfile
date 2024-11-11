FROM openjdk:17-jdk-alpine
EXPOSE 8082
ADD target/tp-foyer-1.0.0.jar tp-foyers.jar
ENTRYPOINT ["java", "-jar", "/tp-foyers.jar"]

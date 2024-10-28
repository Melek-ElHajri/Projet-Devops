FROM openjdk:17-jdk-alpine
EXPOSE 8089
ADD tp-foyer-5.0.0.jar tp-foyer.jar
ENTRYPOINT ["java", "-jar", "/tp-foyer.jar"]

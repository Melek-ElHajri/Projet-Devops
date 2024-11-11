FROM openjdk:17-jdk-alpine
EXPOSE 8089
ADD target/tp-foyer-6.0.0.jar tp-foyer.jar
ENTRYPOINT ["java", "-jar", "/tp-foyer.jar"]

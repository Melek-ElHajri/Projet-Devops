FROM openjdk:17-jdk-alpine
EXPOSE 8089
ADD target/*.jar tp-foyer.jar
ENTRYPOINT ["java", "-jar", "/tp-foyer.jar"]

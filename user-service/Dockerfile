FROM eclipse-temurin:17-jdk-jammy
LABEL authors="VipashynaSharma"
WORKDIR /app
COPY target/user-service-0.0.1-SNAPSHOT.jar user-service.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "user-service.jar"]
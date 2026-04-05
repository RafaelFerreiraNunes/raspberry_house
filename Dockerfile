FROM maven:3.9-amazoncorretto-25 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

FROM ubuntu:24.04

RUN apt-get update && apt-get install -y \
    openjdk-25-jdk-headless \
    libgpiod-dev \
    curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /app/target/application.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
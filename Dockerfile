FROM maven:3.9-amazoncorretto-25 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests

FROM amazoncorretto:25-al2023

WORKDIR /app

COPY --from=build /app/target/application.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
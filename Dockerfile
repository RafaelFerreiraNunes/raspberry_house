FROM maven:3.9-amazoncorretto-25 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

FROM amazoncorretto:25-al2023

RUN yum install -y libgpiod && yum clean all

WORKDIR /app
COPY --from=build /app/target/application.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
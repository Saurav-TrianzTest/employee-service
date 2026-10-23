FROM maven:3.9.9-eclipse-temurin-25 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline
COPY src ./src
RUN mvn -B -ntp package

FROM eclipse-temurin:25-jre-jammy
WORKDIR /app
RUN groupadd --system spring && useradd --system --gid spring --home-dir /app spring \
    && chown spring:spring /app
USER spring
COPY --from=build /workspace/target/employee-service.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

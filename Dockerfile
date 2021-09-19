FROM maven:3.8.2-openjdk-17 AS build
WORKDIR /opt/jnews

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn package

FROM openjdk:17-slim
COPY --from=build /opt/jnews/target/jnews-1.0-SNAPSHOT-shaded.jar jnews.jar
CMD ["java", "-jar", "jnews.jar"]

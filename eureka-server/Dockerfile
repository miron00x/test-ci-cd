FROM maven:3.6-jdk-8 AS builder
COPY pom.xml /eureka-server/pom.xml
COPY src /eureka-server/src
RUN mvn -f /eureka-server/pom.xml clean package

FROM openjdk:8-jre-alpine
COPY --from=builder /eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar ./
EXPOSE 8761
CMD ["java", "-jar", "eureka-server-0.0.1-SNAPSHOT.jar"]

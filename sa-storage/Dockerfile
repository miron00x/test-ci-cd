FROM maven:3.6-jdk-8 AS builder
COPY pom.xml /sa-storage/pom.xml
COPY src /sa-storage/src
RUN mvn -f /sa-storage/pom.xml clean package

FROM openjdk:8-jre-alpine
COPY --from=builder /sa-storage/target/sentiment-analysis-web-0.0.1-SNAPSHOT.jar ./
EXPOSE 8080
CMD ["java", "-jar", "sentiment-analysis-web-0.0.1-SNAPSHOT.jar", "--sa.logic.api.url=${SA_LOGIC_API_URL}"]

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /Pratica6

COPY target/ex.3-0.0.1-SNAPSHOT.jar /Pratica6/app.jar

EXPOSE 8585

CMD ["java", "-jar", "app.jar"]

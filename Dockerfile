FROM eclipse-temurin:17-jdk-alpine

# Set the working directory in the container
WORKDIR /Pratica6

# Copy the JAR file into the container at /educacaoGamificada
COPY target/*.jar /Pratica6/Pratica6-0.0.1-SNAPSHOT.jar

# Expose the port that your application will run on
EXPOSE 8585

# Specify the command to run on container start
CMD ["java", "-jar", "Pratica6-0.0.1-SNAPSHOT.jar"]

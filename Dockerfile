# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim
# Set the working directory in the container
WORKDIR /scm2.0
# Copy the current directory contents into the container at /app
COPY  target/*.jar scm2.0-0.2.jar

EXPOSE 8082
#ADD target/scm2.0-0.0.1-SNAPSHOT.jar scm2.0-0.0.1-SNAPSHOT.jar
#COPY target/scm2.0-0.0.1-SNAPSHOT.war /app/scm2.0-0.0.1-SNAPSHOT.war
ENTRYPOINT ["java", "-jar", "/scm2.0-0.2.jar"]

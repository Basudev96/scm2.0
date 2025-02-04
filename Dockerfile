# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim
EXPOSE 8000
ADD target/scm2.0-0.0.1-SNAPSHOT.war scm2.0-0.0.1-SNAPSHOT.war
#COPY target/scm2.0-0.0.1-SNAPSHOT.war /app/scm2.0-0.0.1-SNAPSHOT.war
ENTRYPOINT ["java", "-jar", "/scm2.0-0.2.war"]

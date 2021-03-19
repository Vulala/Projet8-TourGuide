FROM openjdk:8-jdk-alpine as build 
ARG JAR_FILE=target/*.jar 
COPY Project08-TourGuide-1.0.0.jar TourGuide-1.0.0.jar 
ENTRYPOINT ["java","-jar","/TourGuide-1.0.0.jar"]

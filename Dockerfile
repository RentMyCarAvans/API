# got info from:
# https://medium.com/geekculture/dockerizing-a-spring-boot-application-with-maven-122286e9f582

# AS <NAME> to name this stage as maven
FROM maven:3.6.3 AS maven
LABEL MAINTAINER="RentMyCar@RentMyCar.com"

WORKDIR /usr/src/app
COPY . /usr/src/app
# Compile and package the application to an executable JAR
RUN mvn package

# For Java 17,
FROM openjdk:17-jdk-alpine

ARG JAR_FILE=rent-my-car.jar

WORKDIR /opt/app

# Copy the spring-boot-api-tutorial.jar from the maven stage to the /opt/app directory of the current stage.
COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/

ENTRYPOINT ["java","-jar","rent-my-car.jar"]
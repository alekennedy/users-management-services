FROM maven:3.6.1-jdk-8 AS build
WORKDIR /home
COPY pom.xml /home/
RUN mvn -T 1C dependency:go-offline && mvn -T 1C dependency:resolve-plugins
COPY . /home/ 
RUN cd /home/ && mvn package

FROM openjdk:jre-alpine AS base
USER root

FROM base AS final
WORKDIR /home
COPY --from=build /home/target/UsersManagement-0.0.1-SNAPSHOT.jar /opt/UsersManagement-0.0.1-SNAPSHOT.jar
RUN mkdir /home/.Telemedicina-Services && mkdir /home/.UserManagement-Services
RUN chmod 775 -R /usr/bin/java /home
USER 1001
EXPOSE 8080

ENTRYPOINT ["/bin/sh", "-c", "/usr/bin/java -jar /opt/UsersManagement-0.0.1-SNAPSHOT.jar -Djava.net.preferIPv4Stack=true"]

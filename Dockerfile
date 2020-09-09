
FROM maven:3.6.0-jdk-11-slim AS build

COPY pom.xml /pom.xml
COPY src /src/

RUN mvn clean package -f /pom.xml

# Run stage
FROM openjdk:12

ENV BOT_TOKEN=UNSET

RUN mkdir /config/
COPY --from=build /target/Keeper-jar-with-dependencies.jar /Keeper.jar

CMD /usr/bin/java -jar /Keeper.jar $BOT_TOKEN
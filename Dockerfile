FROM gradle:7.1-jdk16 AS build
COPY --chown=gradle:gradle . /keeper
WORKDIR /keeper
RUN gradle shadowJar --no-daemon

FROM openjdk:11.0.8-jre-slim
RUN mkdir /config/
COPY --from=build /keeper/build/libs/Keeper.jar /

ENTRYPOINT ["java", "-jar", "/Keeper.jar"]
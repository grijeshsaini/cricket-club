FROM gradle:7.6-jdk19 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x testIntegration --no-daemon

FROM openjdk:19-jdk
VOLUME /tmp
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS  -jar /app.jar

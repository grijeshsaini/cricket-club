FROM adoptopenjdk/openjdk16:jre-16_36
VOLUME /tmp
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT exec java $JAVA_OPTS  -jar /app.jar

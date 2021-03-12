FROM openjdk:15-java-alpine
VOLUME /tmp
ADD dataqin-flow-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
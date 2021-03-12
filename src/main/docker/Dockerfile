FROM openjdk:15
VOLUME /tmp
ADD dataqin-flow-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
FROM amazoncorretto:17-alpine

COPY target/GoldenRaspberryAwards-0.0.1-SNAPSHOT.jar app.jar

RUN mvn clean package

ENTRYPOINT ["java", "-jar", "/app.jar"]

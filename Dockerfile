FROM amazoncorretto:17-alpine
COPY target/goldenraspberryawards.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM amazoncorretto:22-alpine-jdk
ARG JAR_FILE=target/*.jar
COPY ./target/todo-app-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
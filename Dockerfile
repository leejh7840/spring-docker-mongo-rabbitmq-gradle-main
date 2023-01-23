FROM openjdk:17-oracle
VOLUME /main-app
ADD build/libs/spring-mongo-docker-rabbitmq-gradle-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
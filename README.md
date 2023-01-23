SpringBoot MongoDB RestAPI Docker RabbitMQ Gradle

This is a model project with sample implementation of RESTApis with MongoDB as NoSQl DB, dockerized application and RabbitMQ.

Requirements
For building and running the application you need:

1. JDK 17
   1. Here is the link to jdk17 download page - https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
   2. Update JAVA_HOME environment variable to point to jdk17.
2. Gradle 7.3.2 
3. Docker
   1. Here is the link to download page for Docker Desktop - https://www.docker.com/products/docker-desktop

RabbitMQ - Docker Image is sufficient
MongoDB  - Docker Image is sufficient


Building
Gradle is the main tool for build & dependency management. You will be able to run gradle commands via the gradle
wrapper in the root of this project, e.g. ./gradlew tasks

./gradlew clean - Deletes the build directory.
./gradlew build - Assembles and tests this project.
docker-compose build - Builds image of the application depending on the mongodb service and rabbitmq service.

Start application

docker-compose up - Starts a container with 4 images. app image,mongo server, mongo express(UI for mongodb) and rabbitmq service.

View it locally on Swagger here:
http://localhost:8080/swagger-ui/index.html
http://localhost:8080/v3/api-docs/
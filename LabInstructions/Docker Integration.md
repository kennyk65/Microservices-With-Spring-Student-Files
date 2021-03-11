## Running solutions with Docker

1. Install Docker and docker-compose 
2. Build docker image of each solution using maven spring-boot:build-image [Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/)

   See example on comment at top of each docker-compose.yml file. Example:
   ```shell script
   ./mvnw -pl lab-3/client-solution,lab-3/server-solution spring-boot:build-image
   ```
3. Run solution containers:
   ```shell script
   cd lab-3
   docker-compose up
   ```

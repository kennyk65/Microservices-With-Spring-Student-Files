##Lab 8 - Using Spring Cloud Bus

  **Part 1 - The Broker**

1.  Download and run Rabbit MQ:
    * If you have Docker installed (recommended) you can easily pull the image and run the container by running `docker run -dp 5672:5672 --hostname my-rabbit --name some-rabbit rabbitmq:3`.  A newer release may be available and recommended by the time you read these instructions, see [RabbitMQ - DockerHub](https://hub.docker.com/_/rabbitmq) for details. 
    * Otherwise, you can download and run RabbitMQ from [https://www.rabbitmq.com/download.html](https://www.rabbitmq.com/download.html).  Use appropriate distribution for your platform.  

1.  Launch Rabbit MQ and leave it running.

    **Part 2 - The Server**

1.  Stop ALL of the services that you may have running from previous exercises, including the "common" eureka and config servers used on previous labs.  If using an IDE you may also wish to close all of the projects that are not related to "lab-8".

1.  Open **lab-8/config-server**.  This project is essentially identical to what you produced in Lab 3.  

1.  Open the POM.  Add dependencies for:
    * Spring Cloud Bus - AMQP (`org.springframework.cloud` /  `spring-cloud-starter-bus-amqp`).
    * Spring Boot Actuator (`org.springframework.boot` / `spring-boot-starter-actuator`).

    >  If using IntelliJ, the Maven extension may require you to update your project at this point.  From the menu, View / Maven / Refresh all...

    >  If using Eclipse, the M2E plugin may require you to update your project at this point.  Right click on the project / Maven / Update Project


1.  Open application.yml.  Change the `spring.cloud.config.server.git.uri` to your own personal git repository.  If you are not sure what this is, take a look back at lab 3 where we first introduced spring cloud config.

1.  Also within application.yml: notice the entry for `management.endpoints.web.exposure.include`.  It includes `bus-refresh`.  This permits external POST requests to trigger the refresh.

1.  Save your work and run the lab-8/config-server.

    **Part 3 - The Client**

1.  Open **lab-8/client** (essentially identical to your client from lab 3).  

1.  Open the POM.  Add dependencies for:
    * Spring Cloud Bus - AMQP (`org.springframework.cloud` /  `spring-cloud-starter-bus-amqp`).
    * Spring Boot Actuator (`org.springframework.boot` / `spring-boot-starter-actuator`).

1.  Open the `LuckyWordController`.  Add a `@ConfigurationProperties` annotation using a prefix of “word-config”.  Notice the properties / getters and setters.

1.  Open application.yml.  Notice the application name.  What file will be needed in your Git repository to hold properties for this application?

    **Part 4 - The Repository**

1.  Within your Git repository, create a lucky-word-client.yml (or .properties) file.  Add a level for “wordConfig:” and entries within it for “luckyWord:” and “preamble:”.  Add a value for lucky word, something like “Irish” or “Serendipity” or “Rabbit’s Foot”.  For preamble add a value such as “The lucky word is”.  If you need a hint take a look at the “ConfigData” folder in the student files.  Save your work and commit to the repository.

    ```
    wordConfig:
      preamble: The lucky word is
      luckyWord: Irish
    ```

    **Part 5 - Running**

1.  Start your **lab-8/client** application.  It should start without errors.

1.  Open a browser to [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word).  You should see output such as “The lucky word is: Irish”.  If you do not, review the previous steps.

    **Part 6 - Configuration Changes**

    Ordinarily, configuration is only retrieved by a Spring application during initialization. The config server has great benefits but does not change this basic fact.  Spring Cloud Bus will allow us to broadcast configuration changes to running applications, and have those changes take effect immediately.

1.  Return to your Git repository and edit your lucky-word-client.yml file.  Change the lucky word to some other values.  Commit your work.  Will this change be visible if you refresh [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word) right now?  Why not?

1.  Make a POST request to http://localhost:8001/actuator/busrefresh.  You can do this using:
    * A “curl” command in Linux / Unix (curl --request POST  http://localhost:8001/actuator/busrefresh)
    * A REST client plugin for one of your browsers (Like "Advanced  REST Client” on Chrome).
    * Postman

1.  Refresh [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word).  Your changes should be visible.  If so, congratulations, you have successfully used Spring Cloud Bus!  You can stop the RabbitMQ server now.


    **Reflection:**  

1. Our solution blasts the configuration changes to all clients.  We could have targeted clients more selectively by altering the POST to the config server.  See the docs for details. 

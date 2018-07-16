##Lab 8 - Using Spring Cloud Bus

  **Part 1 - The Broker**

1.  Download Rabbit MQ from [https://www.rabbitmq.com/download.html](https://www.rabbitmq.com/download.html).  Use appropriate distribution for your platform.  

2.  Launch Rabbit MQ and leave it running.

  **Part 2 - The Server**

3.  Stop ALL of the services that you may have running from previous exercises.  If using an IDE you may also wish to close all of the projects that are not related to "lab-8” or "common".

4.  Open lab-8-config-server.  This project is essentially identical to what you produced in Lab 3.  Open the POM, add another dependency for spring-cloud-starter-bus-amqp.  Also add a dependencies for Spring Boot Actuator (org.springframework.boot / spring-boot-starter-actuator).

5.  Open application.yml.  Change the spring.cloud.config.server.git.uri to your own personal git repository.  If you are not sure what this is, take a look back at lab 3 where we first introduced spring cloud config.

6.  Also within application.yml: notice the entry for management.endpoints.web.exposure.include: bus-refresh .  This permits external POST requests to trigger the refresh.

7.  Save your work and run the lab-8-config-server.

  **Part 3 - The Client**

8.  Open lab-8-client (essentially identical to your client from lab 3).  Open the POM, add another dependency for spring-cloud-starter-bus-amqp.  Also add a dependencies for Spring Boot Actuator (org.springframework.boot / spring-boot-starter-actuator) and Spring Boot Configuration Processor (org.springframework.boot / spring-boot-configuration-processor). 

9.  Open the LuckyWordController.  Add a @ConfigurationProperties annotation using a prefix of “word-config”.  Notice the properties / getters and setters.

10.  Open bootstrap.yml.  Notice the application name.  What file will be needed in your Git repository to hold properties for this application?

  **Part 4 - The Repository**

11.  Within your Git repository, create a lucky-word-client.yml (or .properties) file.  Add a level for “wordConfig:” and entries within it for “luckyWord:” and “preamble:”.  Add a value for lucky word, something like “Irish” or “Serendipity” or “Rabbit’s Foot”.  For preamble add a value such as “The lucky word is”.  If you need a hint take a look at the “ConfigData” folder in the student files.  Save your work and commit to the repository.

  **Part 5 - Running**

12.  Start your lab-8-client application.  It should start without errors.

13.  Open a browser to [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word).  You should see output such as “The lucky word is: Irish”.  If you do not, review the previous steps.

  **Part 6 - Configuration Changes**

14.  Return to your Git repository and edit your lucky-word-client.yml file.  Change the lucky word to some other values.  Commit your work.  Will this change be visible if you refresh [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word) right now?  Why not?

15.  Make a POST request to http://localhost:8001/actuator/bus-refresh.  You can do this using a “curl” command in Linux / Unix (curl --request POST  http://localhost:8001/actuator/bus-refresh), or you can find a REST client plugin for one of your browsers that will do this (I've been using “Simple REST Client” on Chrome).

16.  Refresh [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word).  Your changes should be visible.  If so, congratulations, you have successfully used Spring Cloud Bus!  You can stop the RabbitMQ server now.

  **BONUS - @RefreshScope**

17.  Return to your LuckyWordController and convert to @RefreshScope.  For test purposes, you can make the controller stateful by defining another String variable named fullStatement, and populate it in an init() method marked with a @PostConstruct annotation.  Change the showLuckyWord() method to simply return fullStatement.  Repeat the process to make a change to see if your @RefreshScope worked.

**Reflection:**  

1. Our solution blasts the configuration changes to all clients.  We could have targeted clients more selectively by altering the POST to the config server.  See the docs for details. 

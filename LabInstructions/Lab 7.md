## Lab 7 - Using Hystrix Circuit Breakers

**Part 1, Start existing services**

1.  Stop all of the services that you may have running from previous exercises.  If using an IDE you may also wish to close all of the projects that are not related to "lab-7” or "common".  You may leave the common-config-server and common-eureka-server running.

2.  Start the common-config-server and common-eureka-server.  

3.  Lab 7 has copies of the word server and sentence server that have been converted to use Feign, we will adjust them to use Hystrix too.  Start 5 separate copies of the lab-7-word-server, using the profiles "subject", "verb", "article", "adjective", and "noun".  There are several ways to do this, depending on your preference:
  - If you wish to use Maven, open separate command prompts in the target directory and run these commands:
    - mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=subject"
    - mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=verb"
    - mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=article"
    - mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=adjective"
    - mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=noun"
  - Or if you wish to run from directly within STS, right click on the project, Run As... / Run Configurations... .  From the Spring Boot tab specify a Profile of "subject", UNCHECK JMX port / live bean support, and Run.  Repeat this process (or copy the run configuration) for the profiles "verb", "article", "adjective", "noun".

4.  Check Eureka at [http://localhost:8010](http://localhost:8010).   Any warnings about running a single instance are expected.  Ensure that each of your 5 applications are eventually listed in the "Application" section, bearing in mind it may take a few moments for the registration process to be 100% complete.	

5.  Optional - If you wish, you can click on the link to the right of any of these servers.  Replace the "/info" with "/" and refresh several times.  You can observe the randomly generated words.  

6.  Run the lab-7-sentence-server project.  Refresh Eureka to see it appear in the list.  Test to make sure it works by opening [http://localhost:8020/sentence](http://localhost:8020/sentence).  You should see several random sentences appear.  We will refactor this code to use Hystrix.  

  **Part 2 - Refactor**

7.  First, take a look at the lab-7-sentence-server project.  It has been refactored a bit from previous examples.  There is now a WordService and WordServiceImpl that wraps calls to the Feign clients.  This was mainly done to make the lab instructions easier, so that your code modifications are within one class.

8.  Open the POM.  Add another dependency for spring-cloud-starter-netflix-hystrix.

9.  Edit the main Application configuration class and @EnableHystrix.

10.  Refactor the WordServiceImpl to use Hystrix.  We have decided that it is not strictly necessary to have an adjective in our sentence if the adjective service is failing, so modify the getAdjective service to run within a Hystrix Command.  Establish a fallback method that will return an empty Word (new Word(“”)).

11.  Stop any previously running sentence server and launch your new one.  Test it to make sure it works by opening [http://localhost:8020/sentence](http://localhost:8020/sentence).  The application should work the same as it did before, though the “Adjective” call is now going through a Hystrix circuit breaker.

12.  Locate and STOP the Adjective service.  Refresh [http://localhost:8020/sentence](http://localhost:8020/sentence).  The sentence should appear without an adjective.  Restart the adjective service.  Once the Eureka registration is complete and the circuit breaker re-closes, the sentence server will once again display adjectives.

  **BONUS - Additional Fallbacks**

13.  Refactor the getSubject method so that the hard-coded value “Someone” is substituted whenever the subject service is unavailable.  Refactor the getNoun method so that the hard-coded value “something” is substituted whenever the noun is unavailable.  Experiment with starting and stopping the subject and noun services.

  **BONUS - Add Hystrix Dashboard**

14.  Add the Hystrix Dashboard to your sentence server.  Begin by adding the spring-cloud-starter-netflix-hystrix-dashboard dependency.  Next add @EnableHystrixDashboard annotation to your Application configuration class.  Finally add this property to application.* to allow Actuator to expose the Hystrix stream: 

```
    management: 
      endpoints: 
        web: 
          exposure: 
            include: 'hystrix.stream'
```

15.  Restart the sentence server.  Open [http://localhost:8020/hystrix](http://localhost:8020/hystrix).  When prompted, enter http://localhost:8020/actuator/hystrix.stream as the host to monitor.  

16.  Refresh [http://localhost:8020/sentence](http://localhost:8020/sentence) several times to generate activity.  If you like, stop and restart the subject, noun, and adjective services to observer circuit breakers in use.

  **BONUS - Add Asynchronous Behavior**

17.  If you like, you can attempt to increase the performance of our sentence server by making the service calls “reactively”.

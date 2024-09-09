## Lab 7 - Using Circuit Breakers

**Part 1, Start existing services**

1.  Stop all of the services that you may have running from previous exercises.  If using an IDE you may also wish to close all of the projects that are not related to "lab-7” or "common".  You may leave the common-config-server and common-eureka-server running.

1.  Start the common-config-server and common-eureka-server.  

1.  Lab 7 has copies of the word server and sentence server that have been converted to use Feign.  Start 5 separate copies of the lab-7/word-server, using the profiles "subject", "verb", "article", "adjective", and "noun".  There are several ways to do this, depending on your preference:

    - If you wish to use Maven, open separate command prompts in the target directory and run these commands:
      - mvn spring-boot:run -Dspring.profiles.active=subject
      - mvn spring-boot:run -Dspring.profiles.active=verb
      - mvn spring-boot:run -Dspring.profiles.active=article
      - mvn spring-boot:run -Dspring.profiles.active=adjective
      - mvn spring-boot:run -Dspring.profiles.active=noun

    - If you wish to build the code and run the JAR, run `mvn package` in the project's root.  Then open separate command prompts in the target directory and run these commands:
      - java -jar -Dspring.profiles.active=subject   lab-7-word-server-1.jar 
      - java -jar -Dspring.profiles.active=verb      lab-7-word-server-1.jar 
      - java -jar -Dspring.profiles.active=article   lab-7-word-server-1.jar 
      - java -jar -Dspring.profiles.active=adjective lab-7-word-server-1.jar 
      - java -jar -Dspring.profiles.active=noun      lab-7-word-server-1.jar 

    - **IntelliJ** Open lab-7/word-server.  
      * Use menu "Run" / "Edit Configurations".  
      * Press "+" to add new configuration. Select "Application".  
      * Choose Name=noun, Main class=demo.Application.  
      * Click "Modify Options" / "Add VM Options".  
      * Enter `-Dspring.profile.active=noun` in new field.
      * Apply.  Run.  
      * Repeat this process (or copy the run configuration) for the profiles "verb", "article", "adjective", "noun".

    - **Eclipse/STS** Import lab-7/word-server into your workspace.
      * R-click on the project, Run As... / Run Configurations... .
      * From the Spring Boot tab specify a Profile of "subject", 
      * UNCHECK JMX port / live bean support, and Run.  
      * Repeat this process (or copy the run configuration) for the profiles "verb", "article", "adjective", "noun".

1.  Check Eureka at [http://localhost:8010](http://localhost:8010).   Any warnings about running a single instance are expected.  Ensure that each of your 5 applications are eventually listed in the "Application" section, bearing in mind it may take a few moments for the registration process to be 100% complete.	

1.  Optional - If you wish, you can click on the link to the right of any of these servers.  Replace the "/actuator/info" with "/" and refresh several times.  You can observe the randomly generated words.  

1.  Open and run the lab-7/sentence-server project.  Refresh Eureka to see it appear in the list.  Test to make sure it works by opening [http://localhost:8020/sentence](http://localhost:8020/sentence).  You should see several random sentences appear.  We will refactor this code to use circuit breakers.  

    **Part 2 - Refactor**

1.  First, take a look at the lab-7/sentence-server project.  It has been refactored a bit from previous examples.  There is now a `WordService` and `WordServiceImpl` that wraps calls to the Feign clients.  This was mainly done to make the lab instructions easier, so that your code modifications are within one class.

1.  Open the POM.  Add another dependency for spring-cloud-starter-circuitbreaker-resilience4j.
    > Note that it is possible to use [Spring Retry](https://github.com/spring-projects/spring-retry) or [Sentinel](https://github.com/alibaba/Sentinel) here, but we will stick to one option. 

1.  Refactor the `WordServiceImpl` with circuit breaker logic.  Begin by injecting a `CircuitBreakerFactory` object.

    ```
      @Autowired CircuitBreakerFactory circuitBreakers;
    ```

1.  We have decided that it is not strictly necessary to have an adjective in our sentence.  When the adjective service fails; we will simply produce a sentence with the adjective missing.  Make the following modifications to the `getAdjective()` method:
    * Use the `circuitBreakers` object to create a news circuit breaker named "adjective".

    ```
    		CircuitBreaker cb = circuitBreakers.create("adjective");
    ```

    * Use the circuit breaker's `run()` method to define the normal "run" action and the "fallback" action.  
      * The "run" action is a [Supplier](https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html) which returns the value from `adjectiveClient.getWord()`.
      * The "fallback" action is a [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html) which returns the value from `getFallbackAdjective()`. 

    ```
        return
          cb.run(
            () -> adjectiveClient.getWord(),
            (throwable) -> getFallbackAdjective()
          );
    ```
      * Observe that the `getFallbackAdjective()` simply returns an empty `Word` object. 

1.  If you like, you can implement the entire circuit breaker in a single statement:

    ```
		return
			circuitBreakers
				.create("adjective")	
				.run(
					() -> adjectiveClient.getWord(),
					(throwable) -> getFallbackAdjective()
				);
    ```

1.  Stop any previously running sentence server and launch your new one.  Test it to make sure it works by opening [http://localhost:8020/sentence](http://localhost:8020/sentence).  The application should work the same as it did before, though the “Adjective” call is now going through a circuit breaker.

1.  Locate and STOP the Adjective service.  Refresh [http://localhost:8020/sentence](http://localhost:8020/sentence).  The sentence should appear without an adjective.  Restart the adjective service.  Once the Eureka registration is complete and the circuit breaker re-closes, the sentence server will once again display adjectives.

    **BONUS - Additional Fallbacks**

1.  Refactor the getSubject and / or getNoun methods so that the hard-coded value “Someone” / "Something" is substituted whenever the subject / noun service is unavailable.  Experiment with starting and stopping the subject and noun services.

1.  Experiment with slow-running services.  Run the noun service with the additional profile of "delay" included.  This will introduce a random 0-2 second delay to the response time.  You can configure the sentence server to trip the noun circuit breaker when slow results are common, experiment with the following settings:

    ```
    resilience4j:
      circuitbreaker:
        instances:
          noun:
            slowCallDurationThreshold: 3s 	# Calls over three seconds are “slow”
            slowCallRateThreshold: 50		# If > 50% are slow, trip the breaker
            slidingWindowSize: 5            # Look at last five calls
            minimumNumberOfCalls: 3         # After three calls, start doing calculations
            waitDurationInOpenState: 10s    # After trip, wait ten seconds before trying again
    ```

    * Continually refresh the browser.
    * You should experience the sentence server struggling to retrieve nouns until the breaker trips, then the word "something" is applied almost immediately.
    * You should observe the sentence server slowing down periodically as Resilience4J attempts to close the circuit breaker.  Sometimes it will be successful and you will encounter slow production of diverse nouns.  Other times it will fail and you will encounter fast production of sentences ending in "something".

1.  Experiment with random erro producing services.  Run the subject service with the additional profile of "exception" included.  This will introduce random RuntimeExceptions occurring roughly 50% of the time.  Configure the sentence server to trip the subject circuit breaker when the failure rate exceeds 70%.  Experiment with the following settings:

    ```
      subject:
        failureRateThreshold: 70        # If > 70% fail, trip the breaker
        slidingWindowSize: 10           # Look at last five calls
        minimumNumberOfCalls: 3         # After three calls, start doing calculations
        waitDurationInOpenState: 10s    # After trip, wait ten seconds before trying again
    ```


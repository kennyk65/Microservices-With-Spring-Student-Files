## Lab 6 - Using Feign Declarative REST clients

**Part 1, Start existing services.**

1.  Stop all of the services that you may have running from previous exercises. If using an IDE you may also wish to close all of the projects that are not related to "lab-6" or "common".

1.  Start the common-config-server and the common-eureka-server. These are versions of what you created and used in the last few chapters.

1.  Lab 6 has copies of the word server and sentence server that have been slightly refactored from what we worked on previously.  Start 5 separate copies of the lab-6/word-server, using the profiles "subject", "verb", "article", "adjective", and "noun".  There are several ways to do this, depending on your preference:
    - If you wish to use Maven, open separate command prompts in the target directory and run these commands:
      - mvn spring-boot:run -Dspring.profiles.active=subject
      - mvn spring-boot:run -Dspring.profiles.active=verb
      - mvn spring-boot:run -Dspring.profiles.active=article
      - mvn spring-boot:run -Dspring.profiles.active=adjective
      - mvn spring-boot:run -Dspring.profiles.active=noun

    - If you wish to build the code and run the JAR, run `mvn package` in the project's root.  Then open separate command prompts in the target directory and run these commands:
      - java -jar -Dspring.profiles.active=subject   lab-6-word-server-1.jar 
      - java -jar -Dspring.profiles.active=verb      lab-6-word-server-1.jar 
      - java -jar -Dspring.profiles.active=article   lab-6-word-server-1.jar 
      - java -jar -Dspring.profiles.active=adjective lab-6-word-server-1.jar 
      - java -jar -Dspring.profiles.active=noun      lab-6-word-server-1.jar 

    - **IntelliJ** Open lab-6/word-server.  
      * Use menu "Run" / "Edit Configurations".  
      * Press "+" to add new configuration. Select "Application".  
      * Choose Name=noun, Main class=demo.Application.  
      * Click "Modify Options" / "Add VM Options".  
      * Enter `-Dspring.profiles.active=noun` in new field.
      * Apply.  Run.  
      * Repeat this process (or copy the run configuration) for the profiles "verb", "article", "adjective", "noun".

    - **Eclipse/STS** Import lab-6/word-server into your workspace.
      * R-click on the project, Run As... / Run Configurations... .
      * From the Spring Boot tab specify a Profile of "subject", 
      * UNCHECK JMX port / live bean support, and Run.  
      * Repeat this process (or copy the run configuration) for the profiles "verb", "article", "adjective", "noun".

1.  Check the Eureka server running at [http://localhost:8010](http://localhost:8010).   Ignore any warnings about running a single instance; this is expected.  Ensure that each of your 5 applications are eventually listed in the "Application" section, bearing in mind it may take a few moments for the registration process to be 100% complete.	

1.  Optional - If you wish, you can click on the link to the right of any of these servers.  Replace the "/actuator/info" with "/" and refresh several times.  You can observe the randomly generated words within a JSON structure.  

1.  Run the lab-6/sentence-server project.  Refresh Eureka to see it appear in the list.  Test to make sure it works by opening [http://localhost:8020/sentence](http://localhost:8020/sentence).  You should see several random sentences appear.  We will refactor this code to make use of Feign.

    **Part 2 - Refactor**

1.  First, take a look at the lab-6/sentence-server project.  It has been refactored a bit from previous examples.  
    * The controller has been simplified to do only web work, the task of assembling the sentence is now in the service layer.  
    * The `SentenceService` uses `@Autowire` to reference individual DAO components which have been created to obtain the words from the remote resources.  
    * Since all of the remote resources are structurally the same, there is a fair bit of inheritance in the dao package to make things easy.  But each uses the same Load Balancer technology and `RestTemplate` used previously.

1.  Open the POM.  Add another dependency for Open Feign (`org.springframework.cloud` / `spring-cloud-starter-openfeign`).

    >  If using IntelliJ, the Maven extension may require you to update your project at this point.  From the menu, View / Maven / Refresh all...

    >  If using Eclipse, the M2E plugin may require you to update your project at this point.  Right click on the project / Maven / Update Project

1.  Edit the main `Application` configuration class and `@EnableFeignClients`.

1.  Refactor the `NounDaoImpl` dao to use Feign.  Create a new interface in the dao package called `NounClient`.  Annotate it with `@FeignClient("NOUN")`.

1.  Next, provide the method signature to be implemented by Feign.  To think this through, take a fresh look at the lab-6/word-server `WordController`.  Note the annotation used and return type.  You can actually copy/paste this method signature as-is, except omit the method implementation.

    ```
    @GetMapping("/")
    public Word getWord();
    ```

1.  Edit the `SentenceServiceImpl`.  Replace the private `WordDao nounService` and its associated setter with the `NounClient` you just made. Depending on how you built your `NounClient`, you may need to refactor the `buildSentence()` method slightly.

    ```
      NounClient nounService;
    ```
    ```
      @Autowired
      public void setNounService(NounClient nounService) {
        this.nounService = nounService;
      }	
    ```

1.  Notice that you have probably introduced an error on the `SentenceServiceImplTest` test class.  Make adjustments to accomodate the `NounClient`.

1.  Stop any previously running sentence server (keep the word servers running) and launch your new one.  Test it to make sure it works by opening [http://localhost:8020/sentence](http://localhost:8020/sentence).  The application should work the same as it did before, though now it is using a declarative Feign client to make the noun call.

    **BONUS - Additional Refactoring**

1. If you like, you can also refactor the subject, verb, article, and adjective clients.  This can be done reasonably easy by copy paste.  Remove the old DAO implementations and note that you will no longer have any executable code to maintain or unit test (you still must integration test, of course).


**Reflection:**

1. While we have no DAO code that requires **unit** testing, we still need to perform **integration** testing.  Still, it is nice to be freed of the need to do as much coding.

2. Our application will still fail if we can’t find at least one of each kind of word server.  We will improve this later when we discuss circuit breakers with Hystrix.

3. To improve performance, can we run each of the calls in parallel?  We will improve this later when discussing Hystrix. 

## Lab 4 - Create a Spring Cloud Eureka Server and Client

**Part 1, create server**

1. Create a new Spring Boot application.
    - Name the project "lab-4-eureka-server”, and use this value for the Artifact.  
    - Use JAR packaging and the latest versions of Java.  
    - Boot version 3.3.3 is the most recent at the time of this writing, but you can generally use the latest stable version available. 
    - These instructions were tested with Java 21. 
    - Add the **Eureka Server** (spring-cloud-netflix Eureka Server) dependency.

1. Save an application.yml (or properties) file in the root of your classpath (src/main/resources recommended).  Add the following keys / values (use correct YAML formatting):
    - server.port: 8010
    - spring.application.name=lab-4-eureka-server

1. Add `@EnableEurekaServer` to the Application class.  Save.  Start the server.  Open a browser to [http://localhost:8010](http://localhost:8010) to see the server running.

    **Part 2, create clients**  
    
    In this next section we will create several client applications that will work together to compose a sentence.  The sentence will contain a subject, verb, article, adjective and noun such as “I saw a leaky boat” or “You have the reasonable book”.  5 services will randomly generate the word components, and a 6th service will assemble them into a sentence.

1. Create a new Spring Boot web application.  
    - Name the project "lab-4-subject”, and use this value for the Artifact.  
    - Use JAR packaging and the latest versions of Java.  
    - Use Boot version 3.3.3 or the latest stable version available.  
    - These instructions were tested with Java 21. 
    - Add the **Eureka Discovery Client**,  (spring-cloud-netflix Eureka Server), Spring Web, and Actuator as dependencies.

1. Modify the Application class.  Add `@EnableDiscoveryClient`.

1. Save an `application.yml` (or properties) file in the root of your classpath (src/main/resources recommended).  Add the following key / values (use correct YAML formatting):
    - spring.application.name=lab-4-subject
    - eureka.client.serviceUrl.defaultZone=http://localhost:8010/eureka/
    - words=I,You,He,She,It
    - server.port=\${PORT:${SERVER_PORT:0}}
  (this will cause a random, unused port to be assigned if none is specified)

1. Add a Controller class
    - Place it in the 'demo' package or a subpackage of your choice.
    - Name the class anything you like.  Annotate it with `@RestController`.
    - Add a String member variable named “words”.  Annotate it with `@Value("${words}")`.
    - Add the following method to serve the resource (optimize this code if you like):
    ```
      @GetMapping("/")
      public @ResponseBody String getWord() {
        String[] wordArray = words.split(",");
        int i = (int)Math.round(Math.random() * (wordArray.length - 1));
        return wordArray[i];
      }
    ```

1. Repeat steps 4 thru 7 (copy the entire project if it is easier), except use the following values:
    - Name of application: “lab-4-verb”
    - spring.application.name: “lab-4-verb”
    - words: “ran,knew,had,saw,bought”

1. Repeat steps 4 thru 7, except use the following values:
    - Name of application: “lab-4-article”
    - spring.application.name: “lab-4-article”
    - words: “a,the”

1. Repeat steps 4 thru 7, except use the following values:
    - Name of application: “lab-4-adjective”
    - spring.application.name: “lab-4-adjective”
    - words: “reasonable,leaky,suspicious,ordinary,unlikely”

1. Repeat steps 4 thru 7, except use the following values:
    - Name of application: “lab-4-noun”
    - spring.application.name: “lab-4-noun”
    - words: “boat,book,vote,seat,backpack,partition,groundhog”

1. Create a new Spring Boot web application.  
      - Name the project "lab-4-sentence”, and use this value for the Artifact.  
      - Use JAR packaging and the latest versions of Java and Boot.
      - Add the **Eureka Discovery Client**,  (spring-cloud-netflix Eureka Server), Spring Web, and Actuator as dependencies.

1. Add `@EnableDiscoveryClient` to the Application class.  

1. Save an `application.yml` (or properties) file in the root of your classpath (src/main/resources recommended).  Add the following key / values (use correct YAML formatting):
    - eureka.client.serviceUrl.defaultZone=http://localhost:8010/eureka/
    - server.port: 8020

1. Add a Controller class to assemble and return the sentence:
    - Name the class anything you like.  Annotate it with @RestController.
    - Use @Autowired to obtain a DiscoveryClient (import from Spring Cloud).
    - Add the following methods to serve the sentence based on the words obtained from the client services. (feel free to optimize / refactor this code as you like:

    ```
	@Autowired DiscoveryClient client;
	
	@GetMapping("/sentence")
	public String getSentence() {
	  return String.format("%s %s %s %s %s.",
		  getWord("LAB-4-SUBJECT"),
		  getWord("LAB-4-VERB"),
		  getWord("LAB-4-ARTICLE"),
		  getWord("LAB-4-ADJECTIVE"),
		  getWord("LAB-4-NOUN") );
	}

	public String getWord(String service) {
        List<ServiceInstance> list = client.getInstances(service);
        if (list != null && list.size() > 0 ) {
      	  URI uri = list.get(0).getUri();
	      	if (uri !=null ) {
	      		return (new RestTemplate()).getForObject(uri,String.class);
	      	}
        }
        return null;
	}

    ```

1. Run all of the word services and sentence service.  (Run within your IDE, or build JARs for each one (mvn clean package) and run from the command line (java -jar name-of-jar.jar), whichever you find easiest).  Since each service uses a separate, random port, they should be able to run side-by-side on the same computer.  Open [http://localhost:8020/sentence](http://localhost:8020/sentence) to see the completed sentence.  Refresh the URL and watch the sentence change.
 	
    **BONUS - Refactor to use Spring Cloud Config Server.**  

    We can use Eureka together with the config server to eliminate the need for each client to be configured with the location of the Eureka server

1. Add a new file to your GitHub repository (the same repository used in the last lab) called “application.yml” (or properties).  Because this file is named "application.*", the properties set within apply to all clients of the Config server.  This is great for us as we want all clients to find the Eureka server.  Add the following key / values (use correct YAML formatting):
    - eureka.client.serviceUrl.defaultZone=http://localhost:8010/eureka/ 

1. Open the common/config-server project.  This is essentially the same config server that you produced in lab 3.  Alter the application.yml to point to your own github repository.  Save all and run this server.  (You can use it as the config server for almost all of the remaining labs in this course.)  

1. In each client project, add an additional dependency for spring-cloud-config-client: 

    ```
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
      </dependency>
    ```

1. Edit each client application’s `application.yml` / properties file.  Remove the eureka client serviceUrl defaultZone key/value.  Now we will get this from the config server.

1. In each client project, add the following key/value to `application.yml` (or .properties), using correct formatting: 
    - spring.config.import: "optional:configserver:http://localhost:8001"

1. Make sure the Eureka server is still running.  Start (or restart) each client. Open [http://localhost:8020/sentence](http://localhost:8020/sentence) to see the completed sentence.

1. If you like, you can experiment with moving the “words” properties to the GitHub repository so they can be served up by the config server.  You’ll need to use separate profile sections within the file (yml) or files with names that match the application names (yml or properties).  A single application.yml file would look something like this:

    ```
    ---
    spring.config.activate.on-profile: subject
    words: I,You,He,She,It
    
    ---
    spring.config.activate.on-profile: verb
    words: ran,knew,had,saw,bought

    ---
    spring.config.activate.on-profile: article
    words: a,the

    ---
    spring.config.activate.on-profile: adjective
    words: reasonable,leaky,suspicious,ordinary,unlikely

    ---
    spring.config.activate.on-profile: noun
    words: boat,book,vote,seat,backpack,partition,groundhog  
    ```

    **BONUS - Refactor to use multiple Eureka Servers**  
    
    To make the application more fault tolerant, we can run multiple Eureka servers.  Ordinarily we would run copies on different racks / data centers, but to simulate this locally do the following:

1.  Stop all of the running applications.

1.  Edit your computer's /etc/hosts file (c:\WINDOWS\system32\drivers\etc\hosts on Windows, /etc/hosts on Mac OS, Linux).  Add the following lines and save your work:

    ```
    # START section for Microservices with Spring Course
    127.0.0.1       eureka-primary
    127.0.0.1       eureka-secondary
    127.0.0.1       eureka-tertiary
    # END section for Microservices with Spring Course
    ```
    * This file allows you to make your own DNS names, overriding all other sources.

1.  Within the lab-4-eureka-server project, add application.yml with multiple profiles:
primary, secondary, tertiary.  The server.port value should be 8011, 8012, and 8013 respectively.  The eureka.client.serviceUrl.defaultZone for each profile should point to the "eureka-*" URLs of the other two; for example the primary value should be: http://eureka-secondary:8012/eureka/,http://eureka-tertiary:8013/eureka/

1.  Run the application 3 times, using -Dspring.profiles.active=primary (and secondary, and tertiary) to activate the relevant profile.  The result should be 3 Eureka servers which communicate with each other.
    * Expect to observe errors when the first two start; these errors are simply reporting that the other servers are not available - yet.  This is inevitable.

1.  In your GitHub project, modify the application.properties eureka.client.serviceUrl.defaultZone to include the URIs of all three Eureka servers (comma-separated, no spaces).

    ```
    eureka:
      client:
        serviceUrl:
          defaultZone: http://localhost:8011/eureka/,http://localhost:8012/eureka/,http://localhost:8013/eureka/
    ```

1.  Start all clients.  Open [http://localhost:8020/sentence](http://localhost:8020/sentence) to see the completed sentence.

1.  To test Eureka’s fault tolerance, stop 1 or 2 of the Eureka instances.  Restart 1 or 2 of the clients to ensure they have no difficulty finding Eureka.  Note that it may take several seconds for the clients and servers to become fully aware of which services are up / down.  Make sure the sentence still displays.


**Reflection:**  There are a number of remaining issues with the current application which can be addressed.

1. These services contain duplicated code.  This was done only to make the instructions straightforward.  You can easily implement this system using a single ‘word’ server which selects different words based on a @Profile.  (This is done in the solution)

1. What happens if one of the “word” servers is down?  Right now our sentence will display "null" for that word.  We will improve this later when we discuss circuit breakers with Hystrix.

1. To improve performance, can we run each of the calls in parallel?  We will improve this later when discussing Ribbon and Hystrix.

1. We will see an alternative to the RestTemplate when we discuss Feign.


## Lab 3 - Create a Spring Cloud Config Server and Client

**Part 1 - Config Server:**

1. Create a new Spring Boot application.  Select Maven or Gradle, but these instructions will use Maven.  Name the project "lab-3-server”, and use this value for the Artifact.  Use Jar packaging and the latest versions of Java.  Use a version of Boot 3.3.x or greater.   These instructions were tested with Java 21.  Select dependency `Config Server` (Spring Cloud Config).

1. Open the POM (or Gradle) file.  Observe the following:

    * A “Dependency Management” section is used to define the set of dependencies related to Spring Cloud:

    ```
	<dependencyManagement>
		<dependencies>
		  <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-dependencies</artifactId>
			<version>${spring-cloud.version}</version>
			<type>pom</type>
			<scope>import</scope>
		  </dependency>
		</dependencies>
	</dependencyManagement>
    ```

    * The `spring-cloud.version` property is set in the properties section.  It identifies the "release train"; a set of Spring Cloud dependencies tested and released together:

    ```
	<properties>
		...
        <spring-cloud.version>2023.0.3</spring-cloud.version>
        ...
	</properties>    
    ```

    * The Spring Config Server starter has been defined:

    ```
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-config-server</artifactId>
		</dependency>
    ```

1. Edit the main Application class (probably named Lab3ServerApplication, rename it if you like).  Add the `@EnableConfigServer` to this class.

1. Create a new repository on GitHub to hold your application configuration data.  Call the repository "SpringCloudServerConfiguration" or a name of your choosing.  Note the full URI of the repository, you will need this in a following step.

1. Add a new file to your GitHub repository called "lab-3-client.yml” (or lab-3-client.properties).  Add a key called "lucky-word" and a value of "Irish", "Rabbit's Foot", "Serendipity", or any other value of your choosing.

1. Back in your project, create an `application.yml` (or `application.properties`) file in the root of your classpath (src/main/resources recommended).  Add the key `spring.cloud.config.server.git.uri` and the value "https://github.com/"YOUR-GITHUB-ID"/SpringCloudServerConfiguration", substituting the value for Github ID and repository name as needed.  

1. Also within `application.yml` (or `application.properties`), set the “server.port” to 8001.

1. Run the application.  Open the URL [http://localhost:8001/lab-3-client/default](http://localhost:8001/lab-3-client/default/).  You should see the JSON result that will actually be used by Spring, including the lucky-word you defined earlier.  If the server is not working, review the prior steps to find the issue before moving on.

    **Part 2 - Config Client:**

1. Create a new, separate Spring Boot application.  Select Maven or Gradle, but these instructions will use Maven.Use a version of Boot 3.3.x or greater.  Name the project "lab-3-client", and use this value for the Artifact.  These instructions were tested with Java 21.  You can make this a JAR or WAR project, but the instructions here will assume JAR. Select dependencies for `Spring Web` and `Config Server` (Spring Cloud Config).

1. Open the POM (or Gradle) file.  Observe the following:

    * A “Dependency Management” section is present, just as observed in the server.
    * The `spring-cloud.version` property is set just as it was in the server.
    * The dependencies for `spring-boot-starter-web` and `spring-cloud-starter-config` (not *config-server) are present.    


1. Add a application.yml (or application.properties) file in the root of your classpath (src/main/resources recommended).  Add the following key/values using the appropriate format:
    ```
    spring:
      application:
        name: lab-3-client

      config:
        import: "optional:configserver:http://localhost:8001"    

    server:
      port: 8002
    ```
    * `spring.application.name` is critical; it identifies which client is requesting properties.  The config server could host multiple collections of client properties.
    * `spring.config.import` identifies the location of the config server.
        * `optional:` means failure to contact the config server is not fatal.
    * `server.port` overrides the client's port, as usual.

1. Add a REST controller to obtain and display the lucky word:

    ```
    @RestController
    public class LuckyWordController {
 
      @Value("${lucky-word}") String luckyWord;
  
      @GetMapping("/lucky-word")
      public String showLuckyWord() {
        return "The lucky word is: " + luckyWord;
      }
    }
    ```

1.  Start your client.  Open [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word).  You should see the lucky word message in your browser.

    **BONUS - Missing Config Server:**
    
    A robust microservice design should consider that the config server may be unavailable when starting a client.  The spring cloud client can be configured to require the config server's presence, or consider it optional (as above).  Both are legitimate choices for the application designer.  Let's explore what happens with our existing client application at present.

1. Stop the server.

1. Stop the client.

1. Start the client.
      * The application fails to start because the "lucky-word" property cannot be injected into the `LuckyWordController`.
      
      In this case, the configuration server was marked as optional, but it might as well have been required, since the client has a hard requirement on the lucky word.  A good design should strive to function under less-than-perfect conditions, perhaps by providing a default value.  This can be done two ways:
      
      1. Providing a default value in the `@Value` annotation:

        ```
        @Value("${lucky-word:preparation}") String luckyWord;
        ```
      
      1. Provide a default value in `application.yml`:

        ```
        lucky-word: have a fallback plan
        ```

1. Open the **Client's** application.yml and provide a backup value for the required property:

    ```
    lucky-word: backup
    ```
1. Save your work.  Run the client (do not run the server).  The application should start.  Open [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word) and observe the fall-back value for the required property.


    **BONUS - Profiles:**

1. Start the config server.

1. Create a separate file in your GitHub repository called "lab-3-client-northamerica.yml” (or .properties).  Populate it with the "lucky-word" key and a different value than used in any other locations.

1. Stop the client application.  Modify the application.yml file to contain a key of `spring.profiles.active: northamerica`.  Save, and restart your client.  Access the URL.  Which lucky word is displayed?  (You could also run the application with `-Dspring.profiles.active=northamerica` rather than changing the bootstrap file)

### Reflection:  
22. Notice that the client needed some dependencies for Spring Cloud, and the URI of the Spring Cloud server, but no code was needed to call the server.
1. To mitigate the possibility of an unavailable config server, it is common to run multiple instances of the config server in different racks / zones behind a load balancer.  If it is still unavailable, we can code local default values.
1. What happens if we change a property after client applications have started?  The server picks up the changes immediately, but the client does not.  Later we will see how Spring Cloud Bus and refresh scope can be used to dynamically propagate changes.

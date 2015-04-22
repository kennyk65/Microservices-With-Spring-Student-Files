

Lab 3 - Create a Spring Cloud Config Server and Client

PART 1 - Config Server:

1. Create a new Spring Boot application.  Name the project "lab-3-server”, and use this value for the Artifact.  Use Jar packaging and the latest versions of Java and Boot. No need to select any dependencies.

2. Edit the POM (or gradle) file.  Alter the parent group Id to be "org.springframework.cloud" and artifact to be "spring-cloud-starter-parent".  Version 1.0.0.RELEASE is the most recent stable version at the time of this writing.

3. Add a dependency for group "org.springframework.cloud" and artifact "spring-cloud-config-server".  You do not need to specify a version -- this is already defined in the parent project.

4. Edit the main Application class (probably named Lab3ServerApplication).  Add the @EnableConfigServer to this class.

5. Create a new repository on GitHub to hold your application configuration data.  Call the repository "ConfigData" or a name of your choosing.  Note the full URI of the repository, you will need this in a following step.

6. Add a new file to your GitHub repository called "lab-3-client.yml” (or lab-3-client.properties).  Add a key called "lucky-word" and a value of "Irish", "Rabbit's Foot", "Serendipity", or any other value of your choosing.

7. Back in your project, create an application.yml (or application.properties) file in the root of your classpath (src/main/resources recommended).  Add the key "spring.cloud.config.server.git.uri" and the value "https://github.com/<YOUR-GITHUB-ID>/ConfigData", substituting the value for Github ID and repository name as needed.  Also set the “server.port” to 8001.

8. Run the application.  Open the URL http://localhost:8001/lab-3-client/default/.  You should see the JSON result that will actually be used by Spring.  If the server is not working, review the prior steps to find the issue before moving on.


PART 2 - Config Client:

9. Create a new, separate Spring Boot application.  Name the project "lab-3-client", and use this value for the Artifact.  Add the web dependency.  You can make this a JAR or WAR project, but the instructions here will assume JAR.

10.  Open the POM (or Gradle) file and add a “Dependency Management” section to identify the spring cloud parent pom. (You could simply change the parent entries, but most clients will probably be ordinary applications with their own parents):
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-parent</artifactId>
            <version>1.0.0.BUILD-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

11.  Add a dependency for group "org.springframework.cloud" and artifact "spring-cloud-starter”.  You do not need to specify a version -- this is already defined in the parent pom in the dependency management section.

10. Add a bootstrap.yml (or bootstrap.properties) file in the root of your classpath (src/main/resources recommended).  Add the following key/values using the appropriate format:
spring.application.name=lab-3-client
spring.cloud.config.uri=http://localhost:8001  
server.port=8002

(Note that this file must be "boostrap" -- not "application" -- so that it is read early in the application startup process.  The server.port could be specified in either file, but the URI to the config server affects the startup sequence.)

11. Add a REST controller to obtain and display the lucky word:

@RestController
public class LuckyWordController {
 
  @Value("${lucky-word}") String luckyWord;
  
  @RequestMapping("/lucky-word")
  public String showLuckyWord() {
    return "The lucky word is: " + luckyWord;
  }
}

12.  Start your client.  Open http://localhost:8002/lucky-word.  You should see the lucky word message in your browser.

BONUS - Profiles:
13. Create a separate file in your GitHub repository called "lab-3-client-northamerica.yml” (or .properties).  Populate it with the "lucky-word" key and a different value than used in the original file.

14. Stop the client application.  Modify the boostrap file to contain a key of spring.profiles.active: northamerica.  Save, and restart your client.  Access the URL.  Which lucky word is displayed?  (You could also run the application with -Dspring.profiles.active=northamerica rather than changing the bootstrap file)


Reflection:  
1. Notice that the client needed some dependencies for Spring Cloud, and the URI of the Spring Cloud server, but no code.
2. What happens if the Config Server is unavailable when the “lucky word” application starts?  To mitigate this possibility, it is common to run multiple instances of the config server in different racks / zones behind a load balancer.
3. What happens if we change a property after client applications have started?  The server picks up the changes immediately, but the client does not.  Later we will see how Spring Cloud Bus and refresh scope can be used to dynamically propagate changes.


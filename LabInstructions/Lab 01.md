## Lab 1 - Spring Boot

- In this exercise you will create a simple Spring Boot application.  If you feel you already have a good understanding of Spring Boot you can skip this exercise.  All other exercises in this course will assume you know Spring Boot.

You can use whatever IDE you like; Eclipse, IntelliJ, VSCode, or something else.

**Part 1 - Generate the Project**

1.  Go to [https://start.spring.io](https://start.spring.io) and create a new Spring Boot Project.
    * Use either Maven or Gradle (if you have it installed).  All lab instructions are based on Maven.
    * Use the latest stable releases of Boot and Java.  These instructions were originally tested with Java 1.17, Boot 3.3.3.
    * Use JAR packaging for now, unless you prefer WAR and already have a local server (like Tomcat) installed and ready to run it.
    * Use any values you like for group, artifact, package, description, etc.  I suggest `org.test`, `lab-1-boot`, `demo` respectively.
    * Select the following dependencies: Web, Thymeleaf, JPA, HSQLDB, and Actuator.

1. Generate the project, download it, extract it, and open it in your IDE:

    * If using **IntelliJ**

    1. Launch IntelliJ.
    1. Use “Open” to open the project.
    1. Select the folder containing your project files.  This is the folder containing your Maven `pom.xml` or `build.gradle` files.
    1. If prompted about which configuration to use, select either Gradle or Maven, depending on your preference.
    1. Give IntelliJ a moment to digest this project, especially if this is the first project worked with since a fresh installation.

    * If using **VSCode**

    1. Make sure you have installed the "Extension Pack for Java™ by Microsoft" and "Test Runner for Java" extensions.
    1. Use "File" / "Open Folder". Select the root of the folder where your project files are located.  This is the folder containing your Maven `pom.xml` or `build.gradle` files.
        * Give VS Code a moment to initialize its workspace, especially if this is the first time importing a Gradle/Maven project.
        * If you see a message about enabling null analysis for the project, you can select either enable or disable.
        * If you see a message about installing _Extension Pack for Java_, take the install option.
        * If you see a message _Do you trust the authors of the files in this folder_, check the checkbox and click the "trust" button..

    * **Eclipse**

      1. Launch Eclipse.
      1. In the *Workspace Launcher* dialog, create a new workspace in some location you will remember, like **C:\workspace**. Click *Launch*.
      1. Close the *Welcome* panel if open.
      1. If using Gradle, you probably need to update it:
          1. From the menu, select *Help > Eclipse Marketplace*.
          1. In the Find box, type *gradle* and hit enter.
          1. Locate **Buildship Gradle Integration 3.0** and click the __Installed__ button.
              * This will take you to the *Installed* tab.
          1. Locate **Buildship Gradle Integration 3.0** and click the *Update* button.
          1. Accept the license agreement and click *Finish*.
          1. Wait for the installation to be completed.
              * You may see a Security warning, if so, click *Install anyway*
          1. A dialog will open when the updates are installed, click Restart Now.
              * We are ready to work with Gradle.
      1. From the main menu, select File / Import.
      1. In the wizard select either Maven or Gradle depending on the project type.  Then select either Existing Gradle Project or Existing Maven project. Next.
      1. Use “Browse” and navigate to the folder containing your project files.  This is the folder containing your Maven `pom.xml` or `build.gradle` files.  Finish.
      1. Give Eclipse a few moments to digest this project, especially if this is the first project worked with since a fresh installation.
      1. Verify that there are no compilation problems.

**Part 2 - Simple Web Application**

2.  Create a new Controller in the base package:
    * Name the controller anything you like.  
    * Annotate the Controller with `@Controller`.

1.  Create a new method in the controller:
    * Name the method anything you like.  Have it return a String.  No parameters needed.
    * Annotate the method with `@GetMapping("/")`
    * Have the method return the String value "hello".

1.  If not already present, create a new folder under src/main/resources called "templates"

1.  Create a new file in the templates folder called "hello.html".  Place the words "Hello from Thymeleaf" (or any markup you like) within the file.

1.  Save all your work.  

1. Find the main application class in `src/main/java/com/demo`.  It is probably named `Lab1BootApplication.java` or something similar (We usually rename ours to `Application.java`). Run your application.
    * **VS Code**: Right-click, Run Java.  Or open the file and click the “Run” option hovering over the main() method.
    * **IntelliJ**: Right-click, select “Run ‘Application.main()’”. 
    * **Eclipse**: Right-click, Select Run As / Java Application.

1.  Open a browser to [http://localhost:8080/](http://localhost:8080/).  You should see your web page.


**Part 3 - Return a RESTful Response**
  
9.  Create a new Java class called "Team" in the base package.  Give it a Long field for id, and String fields for name, location, and mascot (or whatever other properties you like).  Generate "getters and setters" for all fields. Save your work.

1.  Create a new Controller called "TeamController".  Annotate it with @RestController.

1.  Create a new method in the TeamController.
    - Name the method "getTeams".  Have it return a List of Team objects.  
    - Annotate the method with @GetMapping("/teams")
    - Have the method create a List of Team objects.  Create one or more Team objects and add it to the list.  Set the teams' properties to whatever values you like, and return the List.  Sample:
    ```
    @GetMapping("/teams")
    public List<Team> getTeams() {
      List<Team> list = new ArrayList<>();

      Team team = new Team();
      team.setId(0l);
      team.setLocation("Harlem");
      team.setName("Globetrotters");
      list.add(team);
      
      team = new Team();
      team.setId(1l);
      team.setLocation("Washington");
      team.setName("Generals");
      list.add(team);
      
      return list;
    }

    ```

1.  Save all work.  Stop the application if it is already running, and start it again.  Open [http://localhost:8080/teams](http://localhost:8080/teams).  You should see a JSON response with your teams' data.

**Part 4 - Create Spring Data JPA Repositories**
  
13.  Return to the Team class.  Add required JPA annotations:  The class must be annotated with @Entity, the id must be annotated with @Id and @GeneratedValue.

1.  Create a new Interface called "TeamRepository".  Have it extend CrudRepository<Team,Long>.
    - Be sure to create this as an Interface, not a Class!
  
1.  Open the application's main configuration / launch class (the one annotated with @SpringBootApplication).  Use @Autowired to dependency inject a member variable of type TeamRepository.  Name the variable anything you like (may I suggest: "teamRepository").

1.  Add some logic to initially populate the database:  Add a public void init() method.  Annotate it with @PostConstruct.  Cut and paste the team-creation code from you controller to this method, except rather than returning a List of Teams, call the saveAll() method on the repository.  Also, remove any values set for the team IDs.  Example code:
    ```
      public void init() {
      List<Team> list = new ArrayList<>();

      Team team = new Team();
      team.setLocation("Harlem");
      team.setName("Globetrotters");
      list.add(team);
      
      team = new Team();
      team.setLocation("Washington");
      team.setName("Generals");
      list.add(team);

      teamRepository.saveAll(list);
    }    
    ```

1.  Return to the TeamController.  Use @Autowired to dependency inject a TeamRepository variable.  Name the variable anything you like (may I suggest: "teamRepository").

1.  Alter the logic in your controller method to simply return the result of the repository's findAll() method:
    ```
    @GetMapping("/teams")
    public Iterable<Team> getTeams() {
      return teamRepository.findAll();
    }
    ```

1.  Add this property to application.properties to control how the JPA implementation (Hibernate) should handle the DB schema:
    ```
          spring.jpa.hibernate.ddl-auto = update
    ```

1.  Save all work.  Stop the application if it is already running, and start it again.  Open [http://localhost:8080/teams](http://localhost:8080/teams).  You should see a JSON response with your teams' data.


**Part 5 (Optional)- Create a Single Team endpoint**
  
21.  Return to the TeamController and add a method that returns a single Team given an ID.
    - Name the method anything you like.  Suggestion: getTeam.
    - Return type should be a Team.
    - Use a @GetMapping annotation to map this method to the "/teams/{id}" pattern.
    - Define a parameter named "id" of type Long annotated with @PathVariable.
    - Logic: return the result of the teamRepository's findById(id).get() method.  (The findById() returns a Java 8 "Optional", and the get() simply returns the actual Team.

1.  Save all work.  Stop the application if it is already running, and start it again.  Use [http://localhost:8080/teams](http://localhost:8080/teams) to note the generated ID values for each Team.  Then use URLs such as  [http://localhost:8080/teams/1](http://localhost:8080/teams/1) or [http://localhost:8080/teams/2](http://localhost:8080/teams/2) to get results for the individual teams.

  
**Part 6 - Add Players**

23.  Add a new class named Player.  Add fields for id, name, and position.  The id should be Long, and other fields can be Strings.  Generate getters / setters for each field.  Add an @Entity annotation on the class, and @Id and @GeneratedValue annotations on the id.   You may wish to add a custom constructor to make it easy to create a Player object by supplying name and position Strings.  (If you do this, be sure to retain a default constructor).  Save your work.

1.  Open the Team class.  Add a Set of Player objects named players.  Generate getters and setters.  Annotate the set with 	@OneToMany(cascade=CascadeType.ALL) and @JoinColumn(name="teamId"). You may wish to add a custom constructor to make it easy to create a Team object by supplying name, location, and Set of Players.  (If you do this, be sure to retain a default constructor).  Save your work.

1.  Return to application's main configuration / launch class and alter the team population logic to add some players to each team.  Here is an example implementation:

    ```
      @PostConstruct
    public void init() {
      List<Team> list = new ArrayList<>();

      Set<Player> set = new HashSet<>();
      set.add(new Player("Big Easy", "Showman"));
      set.add(new Player("Buckets", "Guard"));
      set.add(new Player("Dizzy", "Guard"));
      
      list.add(new Team("Harlem", "Globetrotters", set));
      list.add(new Team("Washington","Generals",null));

      teamRepository.saveAll(list);
    }   
    ```

1.  Save all work.  Restart the application.  Open [http://localhost:8080/teams](http://localhost:8080/teams) to see the players.

**Part 7 (Optional) - Add XML Support**

27. When possible, REST applications should return the content type requested by the client.  Spring Boot can easily support formats like XML automatically based on classpath dependencies (like Jackson).  Open your POM and add dependencies for Jackson Dataformat:

    ```
    <dependency>
        <groupId>com.fasterxml.jackson.dataformat</groupId>
        <artifactId>jackson-dataformat-xml</artifactId>
    </dependency>
    ```

1.  Add these properties to your application.properties file to allow control of the desired format through a query parameter:

    ```
      # URL ending with ?format=xml can specify requested media type:
      spring.mvc.contentnegotiation.favor-parameter=true
    ```

1.  Save all work.  Restart the application.  Open [http://localhost:8080/teams/1?format=xml](http://localhost:8080/teams/1?format=xml) to see the results in XML.  Use [http://localhost:8080/teams/1?format=json](http://localhost:8080/teams/1?format=json) to see the results in JSON.
  - Note that not all browsers display JSON & XML nicely, consider shopping for some plugins.
  - Note that the Accept header (Accept=text/xml or Accept=application/json) can also be used to specify the desired content type - it's just a bit more work to set from the browser.  Use of an extension basis for content negotiation (i.e. .xml, .json) has fallen out of favor recently, see [suffix-based parameter matching](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-requestmapping-suffix-pattern-match).
  
**Part 8 - Add Spring Data REST**

30.  Open the project's POM.  Add a dependency for group org.springframework.boot and artifact spring-boot-starter-data-rest.  Save your work.

1.  Open TeamRepository.  Add a @RestResource(path="teams", rel="team") annotation to the interface.

1.  Create a new Interface called "PlayerRepository".  Have it extend CrudRepository<Player,Long>.  (Be sure to create this as an Interface, not a Class)!  Add a @RestResource(path="players", rel="player") annotation to the interface.

1.  Open TeamController.  Comment out the @RestController annotation on the class.  (We will be using Spring Data REST to implement the controller, so we don't want this existing controller to interfere).

1.  Save all work.  Restart the application.  Open [http://localhost:8080/teams](http://localhost:8080/teams) to see the players.  Note that (depending on the browser you are using) you can navigate the links for players and teams.
  - Note that Spring Data REST does not presently support XML, so the format=xml or .xml will have no effect (unfortunately).

  If you have reached this point, Congratulations, you have finished the exercise!


**Part 9 (Optional) - Explore the Actuator Endpoints**

35.  One of the dependencies we specified was Actuator.  It automatically adds some useful endpoints to our web application.  Open the following with a browser:

      * [/actuator/info](http://localhost:8080/actuator/info)
      * [/actuator/health](http://localhost:8080/actuator/health)    

1.  Notice that some other actuator endpoints are not enabled by default.  Try the following - they won't work, but take a close look at the reason why - exposing these could be a security risk:
      * [/actuator/beans](http://localhost:8080/actuator/beans)
      * [/actuator/configprops](http://localhost:8080/actuator/configprops)
      * [/actuator/autoconfig](http://localhost:8080/actuator/env)

1.  Enable these actuator endpoints by adding the following setting to your application.properties (save your work and restart):
      * management.endpoints.web.exposure.include=beans,configprops,mappings,env
 
1.  Explore [/actuator/mappings](http://localhost:8080/actuator/mappings).  This is a useful one for debugging web applications.  Search through and see where the @GetMappings you set earlier are setup.

**Part 10 (Optional) - DevTools**
  
39.  Often while developing we need to run an application, then make some changes, then restart.  The Spring Boot "DevTools" dependency can make things easier by automatically restarting when changes are detected  (though it is not as full-featured as other tools like JRebel).  Add the following dependency: 

      ```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>  
      ```
  
1.  While your application is running, make a small, innocent change to some code (like a comment or spacing).  Observe that depending on the change, DevTools will restart your application.  

Tips:
- When running in Eclipse or STS, you can get automatic hot-swapping of many code changes without DevTools if you "debug" the application rather than "run" it.
- If you encounter unusual issues in the upcoming labs, it may be useful to remove the DevTools dependency to see if the problem persists.

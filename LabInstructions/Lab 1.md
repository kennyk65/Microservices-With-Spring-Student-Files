## Lab 1 - Spring Boot

- In this exercise you will create a simple Spring Boot application.  If you feel you already have a good understanding of Spring Boot you can skip this exercise.  All other exercises in this course will assume you know Spring Boot.

**Part 1 - Simple Web Application**

1.  Using either Spring Tool Suite, or [https://start.spring.io](https://start.spring.io), create a new Spring Boot Project.
  - Use either Maven or Gradle (if you have it installed).  All lab instructions are based on Maven.
  - Use the latest stable releases of Boot and Java.  These instructions were originally tested with Java 1.8, Boot 1.5.1.RELEASE.
  - Use JAR packaging for now, unless you prefer WAR and already have a local server (like Tomcat) installed and ready to run it.
  - Use any values you like for group, artifact, package, description, etc.
  - Select the following dependencies: Web, Thymeleaf, JDBC, JPA, HSQLDB, Actuator.

2.  Create a new Controller in the base package:
  - Name the controller anything you like.  
  - Annotate the Controller with @Controller.

3.  Create a new method in the controller:
  - Name the method anything you like.  Have it return a String.  No parameters needed.
  - Annotate the method with @RequestMapping("/")
  - Have the method return the String value "hello".

4.  If not already present, create a new folder under src/main/resources called "templates"

5.  Create a new file in the templates folder called "hello.html".  Place the words "Hello from Thymeleaf" (or any markup you like) within the file.

6.  Save all your work.  Run your application.
  - If you are working in Spring Tool Suite, simply right-click on the application / Run As / Spring Boot App.
  - If you are working in another IDE, Run the main method found within the main Application class.  
  - If you wish to run from a command prompt, from the application's root folder run mvn spring-boot:run. 
  
7.  Open a browser to [http://localhost:8080/](http://localhost:8080/).  You should see your web page.


  **Part 2 - Return a RESTful Response**
  
9.  Create a new Java class called "Team" in the base package.  Give it a Long field for id, and String fields for name, location, and mascotte (or whatever other properties you like).  Generate "getters and setters" for all fields. Save your work.

10.  Create a new Controller called "TeamController".  Annotate it with @RestController.

11.  Create a new method in the TeamController.
  - Name the method "getTeams".  Have it return a List of Team objects.  
  - Annotate the method with @RequestMapping("/teams")
  - Have the method create a List of Team objects.  Create one or more Team objects and add it to the list.  Set the teams' properties to whatever values you like, and return the List.  Sample:
  ```
	@RequestMapping("/teams")
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

12.  Save all work.  Stop the application if it is already running, and start it again.  Open [http://localhost:8080/teams](http://localhost:8080/teams).  You should see a JSON response with your teams' data.

  **Part 3 - Create Spring Data JPA Repositories**
  
13.  Return to the Team class.  Add required JPA annotations:  The class must be annotated with @Entity, the id must be annotated with @Id and @GeneratedValue.

14.  Create a new Interface called "TeamRepository".  Have it extend CrudRepository<Team,Long>.
  - Be sure to create this as an Interface, not a Class!
  
15.  Open the application's main configuration / launch class (the one annotated with @SpringBootApplication).  Use @Autowired to dependency inject a member variable of type TeamRepository.  Name the variable anything you like (may I suggest: "teamRepository").

16.  Add some logic to initially populate the database:  Add a public void init() method.  Annotate it with @PostConstruct.  Cut and paste the team-creation code from you controller to this method, except rather than returning a List of Teams, call the save() method on the repository.  Also, remove any values set for the team IDs.  Example code:
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

		teamRepository.save(list);
	}    
  ```

17.  Return to the TeamController.  Use @Autowired to dependency inject a TeamRepository variable.  Name the variable anything you like (may I suggest: "teamRepository").

18.  Alter the logic in your controller method to simply return the result of the repository's findAll() method:
  ```
	@RequestMapping("/teams")
	public Iterable<Team> getTeams() {
		return teamRepository.findAll();
	}
  ```
19.  Save all work.  Stop the application if it is already running, and start it again.  Open [http://localhost:8080/teams](http://localhost:8080/teams).  You should see a JSON response with your teams' data.


  **Part 4 (Optional)- Create a Single Team endpoint**
  
20.  Return to the TeamController and add a method that returns a single Team given an ID.
  - Name the method anything you like.  Suggestion: getTeam.
  - Return type should be a Team.
  - Use a @RequestMapping annotation to map this method to the "/teams/{id}" pattern.
  - Define a parameter named "id" of type Long annotated with @PathVariable.
  - Logic: return the result of the teamRepository's findOne() method.

19.  Save all work.  Stop the application if it is already running, and start it again.  Use [http://localhost:8080/teams](http://localhost:8080/teams) to note the generated ID values for each Team.  Then use URLs such as  [http://localhost:8080/teams/1](http://localhost:8080/teams/1) or [http://localhost:8080/teams/2](http://localhost:8080/teams/2) to get results for the individual teams.

  
  **Part 5 - Add Players**

20.  Add a new class named Player.  Add fields for id, name, and position.  The id should be Long, and other fields can be Strings.  Generate getters / setters for each field.  Add an @Entity annotation on the class, and @Id and @GeneratedValue annotations on the id.   You may wish to add a custom constructor to make it easy to create a Player object by supplying name and position Strings.  (If you do this, be sure to retain a default constructor).  Save your work.

21.  Open the Team class.  Add a Set of Player objects named players.  Generate getters and setters.  Annotate the set with 	@OneToMany(cascade=CascadeType.ALL) and @JoinColumn(name="teamId"). You may wish to add a custom constructor to make it easy to create a Team object by supplying name, location, and Set of Players.  (If you do this, be sure to retain a default constructor).  Save your work.

22.  Return to application's main configuration / launch class and alter the team population logic to add some players to each team.  Here is an example implementation:

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

		teamRepository.save(list);
	}   
  ```

23.  Save all work.  Restart the application.  Open [http://localhost:8080/teams](http://localhost:8080/teams) to see the players.


  **Part 6 - Add Spring Data REST**
24.  Open the project's POM.  Add a dependency for group org.springframework.boot and artifact spring-boot-starter-data-rest.  Save your work.

25.  Open TeamRepository.  Add a @RestResource(path="teams", rel="team") annotation to the interface.

26.  Create a new Interface called "PlayerRepository".  Have it extend CrudRepository<Player,Long>.  (Be sure to create this as an Interface, not a Class)!  Add a @RestResource(path="players", rel="player") annotation to the interface.

27.  Open TeamController.  Comment out the @RestController annotation on the class.  (We will be using Spring Data REST to implement the controller, so we don't want this existing controller to interfere).

28.  Save all work.  Restart the application.  Open [http://localhost:8080/teams](http://localhost:8080/teams) to see the players.  Note that (depending on the browser you are using) you can navigate the links for players and teams.

  If you have reached this point, Congratulations, you have finished the exercise!

  **Part 7 (Optional) - Explore the Actuator Endpoints**

29.  One of the dependencies we specified was Actuator.  It automatically adds some useful endpoints to our web application.  Open the following with a browser:
  - [/info](http://localhost:8080/info)
  - [/health](http://localhost:8080/health)

30.  Notice that some other actuator endpoints are not enabled by default.  Try the following - they won't work, but take a close look at the reason why - exposing these could be a security risk:
  - [/beans](http://localhost:8080/beans)
  - [/configprops](http://localhost:8080/configprops)
  - [/autoconfig](http://localhost:8080/autoconfig)

31.  Enable these actuator endpoints by modifying your POM: Add a dependency for group org.springframework.boot and artifact spring-boot-starter-security.  Save your work and restart.   Look at the console output for "default security password".  Copy this randomly-generated password, then browse to endpoints listed above, using "user" for username and paste the value for password.  (Note that this password will regenerate on each restart, set security.user.name and security.user.password to establish static values).
 
32.  Explore [/mappings](http://localhost:8080/mappings).  Does it show you any other useful endpoints you would like to try?

  **Part 8 (Optional) - DevTools**
  
33.  Often while developing we need to run an application, then make some changes, then restart.  The Spring Boot "DevTools" dependency can make things easier by automatically restarting when changes are detected  (though it is not as full-featured as other tools like JRebel).  Add the following dependency: 

  ```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <optional>true</optional>
    </dependency>  
  ```
  
34.  While your application is running, make a small, innocent change to some code (like a comment or spacing).  Observe that depending on the change, DevTools will restart your application.  

Tips:
- When running in Eclipse or STS, you can get automatic hot-swapping of many code changes without DevTools if you "debug" the application rather than "run" it.
- If you encounter unusual issues in the upcoming labs, it may be useful to remove the DevTools dependency to see if the problem persists.

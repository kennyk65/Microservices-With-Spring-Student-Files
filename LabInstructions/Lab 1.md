##Lab 1 - Spring Boot

- In this exercise you will create a simple Spring Boot application.  If you feel you already have a good understanding of Spring Boot you can skip this exercise.  All other exercises in this course will assume you know Spring Boot.

**Part 1 - Simple Web Application**

1.  Using either Spring Tool Suite, or [https://start.spring.io](https://start.spring.io), create a new Spring Boot Project.
  - Use either Maven or Gradle (if you have it installed).  All lab instructions are based on Maven.
  - Use the latest stable releases of Boot and Java.  These instructions were originally tested with Java 1.8, Boot 1.2.3.
  - Use JAR packaging for now, unless you prefer WAR and already have a local server (like Tomcat) installed and ready to run it.
  - Use any values you like for group, artifact, package, description, etc.
  - Select the following dependencies: Web, Thymeleaf, JDBC, HSQLDB, Actuator.

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
  - If you are working in another IDE, Run the main method found within the main Appliction class.  
  - If you wish to run from a command prompt, from the application's root folder run mvn spring-boot:run. 
  
7.  Open a browser to [http://localhost:8080/](http://localhost:8080/).  You should see your web page.

  **Part 2 - Explore the Actuator Endpoints**

8.  One of the dependencies we specified was Actuator.  It automatically adds some useful endpoints to our web application.  Open the following with a browser:
  - [/info](http://localhost:8080/info)
  - [/beans](http://localhost:8080/beans)
  - [/configprops](http://localhost:8080/configprops)
  - [/autoconfig](http://localhost:8080/autoconfig)
  - [/health](http://localhost:8080/health)

8.  Explore [/mappings](http://localhost:8080/mappings).  Does it show you any other useful endpoints you would like to try?

  **Part 3 - Return a RESTful Response**
  
9.  Create a new Java class called "Team" in the base package.  Give it a Long field for id, and String fields for name, location, and mascotte (or whatever other properties you like).  Generate "getters and setters" for all fields. Save your work.

10.  Create a new Controller called "TeamController".  Annotate it with @RestController.

11.  Create a new method in the TeamController.
  - Name the method anything you like.  Have it return a Team.  No parameters needed.
  - Annotate the method with @RequestMapping("/teams/1")
  - Have the method create a new Team, set the team's properties to whatever values you like, and return it.  Sample:
  ```
      	@RequestMapping("/team")
        public Team theTeam() {
          Team team = new Team();
		      team.setLocation("Harlem");
		      team.setName("Globetrotters");
		      return team;
	      }
  ```

12.  Save all work.  Stop the application if it is already running, and start it again.  Open [http://localhost:8080/teams/1](http://localhost:8080/teams/1).  You should see a JSON response with your team's data.

  **Part 4 - Create Spring Data JPA Repositories**
13.  Create a new Interface called "TeamRepository".  Have it extend CrudRepository<Team,Long>.
  - Be sure to create this as an Interface, not a Class!
  
14.  Open the application's main configuration / launch class (the one annotated with @SpringBootApplication).  


Tips:
When running in Eclipse or STS, you can get automatic hot-swapping of many code changes if you "debug" the application rather than "run" it.



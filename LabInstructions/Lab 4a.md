## Lab 4a - Service Discovery with Prometheus and Grafana

In an earlier lab, we used Prometheus and Grafana to monitor our services.  Unfortunately, we had to supply Prometheus with hard-coded URLs. Now that we have established Eureka-based service discovery, we can adapt our Prometheus-based monitoring to dynamically find and monitor all running services.

In this lab you will configure Prometheus to use Eureka service discovery to find all services to monitor.  The changes to our Spring Boot-based apps will me very minor, limited to configuration.

This lab requires Docker.

**Part 1 - Alter the Eureka Server:**

1. Open the lab-4a/eureka-server project.  This is essentially the solution from lab 4.

1. Open the pom.xml file.

1. **TODO-01**: Add the Micrometer dependency (Actuator is already present):
    ```
        <dependency>
          <groupId>io.micrometer</groupId>
          <artifactId>micrometer-registry-prometheus</artifactId>
          <scope>runtime</scope>
        </dependency>		
    ```

1.  Save your work.

    >  If using IntelliJ, the Maven extension may require you to update your project at this point.  From the menu, View / Maven / Refresh all...

    >  If using Eclipse, the M2E plugin may require you to update your project at this point.  Right click on the project / Maven / Update Project

1. Open `src/main/resources/application.yml`.

1. **TODO-02:** Add "prometheus" to the list of exposed endpoints (keep any other endpoints if you wish).  Also set the management.metrics.tags.application: to ${spring.application.name} to dynamically emit the same application name as defined above: 

    ```
        management:
          endpoints.web.exposure.include: health,info,prometheus
          metrics:
            tags:
              application: ${spring.application.name}
    ```

1. Save your work.

1. Open `src/main/java/demo.Application`.  Run the application.
    * The application should run without errors.  If not, stop and review the previous steps.

1. Open a web browser.  Open http://localhost:8010.  You should see the Eureka home page; no instances are registered yet.

    **Part 2 - Alter the Client:**

    Next you will perform the same essential steps for the client.

1. Open the lab-4a/client project.  Open the pom.xml file.

1. **TODO-03**: Add the Micrometer dependency (Actuator is already present).  Save your work:
    ```
        <dependency>
          <groupId>io.micrometer</groupId>
          <artifactId>micrometer-registry-prometheus</artifactId>
          <scope>runtime</scope>
        </dependency>		
    ```

    >  If using IntelliJ, the Maven extension may require you to update your project at this point.  From the menu, View / Maven / Refresh all...

    >  If using Eclipse, the M2E plugin may require you to update your project at this point.  Right click on the project / Maven / Update Project

1. Open `src/main/resources/application.yml`.

1. **TODO-02:** Add "prometheus" to the list of exposed endpoints (keep any other endpoints if you wish).  Also set the management.metrics.tags.application: to ${spring.application.name} to dynamically emit the same application name as defined above: 

    ```
        management:
          endpoints.web.exposure.include: health,info,prometheus
          metrics:
            tags:
              application: ${spring.application.name}
    ```

1. Save your work.

1.  Start 5 separate copies of the lab-4a/eureka-client, using the profiles "subject", "verb", "article", "adjective", "noun", and "sentence".  There are several ways to do this, depending on your preference:

    - If you wish to use Maven, open separate command prompts in the target directory and run these commands:
      - mvn spring-boot:run -Dspring.profiles.active=subject
      - mvn spring-boot:run -Dspring.profiles.active=verb
      - mvn spring-boot:run -Dspring.profiles.active=article
      - mvn spring-boot:run -Dspring.profiles.active=adjective
      - mvn spring-boot:run -Dspring.profiles.active=noun
      - mvn spring-boot:run -Dspring.profiles.active=sentence

    - If you wish to build the code and run the JAR, run `mvn package` in the project's root.  Then open separate command prompts in the target directory and run these commands:
      - java -jar -Dspring.profiles.active=subject   lab-4a-eureka-client-1.jar 
      - java -jar -Dspring.profiles.active=verb      lab-4a-eureka-client-1.jar 
      - java -jar -Dspring.profiles.active=article   lab-4a-eureka-client-1.jar 
      - java -jar -Dspring.profiles.active=adjective lab-4a-eureka-client-1.jar 
      - java -jar -Dspring.profiles.active=noun      lab-4a-eureka-client-1.jar 
      - java -jar -Dspring.profiles.active=sentence  lab-4a-eureka-client-1.jar 

    - **IntelliJ** Open lab-4a/eureka-client.  
      * Use menu "Run" / "Edit Configurations".  
      * Press "+" to add new configuration. Select "Application".  
      * Choose Name=noun, Main class=demo.Application.  
      * Click "Modify Options" / "Add VM Options".  
      * Enter `-Dspring.profiles.active=noun` in new field.
      * Apply.  Run.  
      * Repeat this process (or copy the run configuration) for the profiles "verb", "article", "adjective", "noun", "sentence".

    - **Eclipse/STS** Import lab-4a/eureka-client into your workspace.
      * R-click on the project, Run As... / Run Configurations... .
      * From the Spring Boot tab specify a Profile of "subject", 
      * UNCHECK JMX port / live bean support, and Run.  
      * Repeat this process (or copy the run configuration) for the profiles "verb", "article", "adjective", "noun", "sentence".

1.  Check the Eureka server running at [http://localhost:8010](http://localhost:8010).  Ensure that each of your six client applications are eventually listed in the "Application" section, bearing in mind it may take a few moments for the registration process to be 100% complete.	

1.  If you like, [View the sentence](http://localhost:8020/sentence).  You can also find the Prometheus formatted metrics at [the Prometheus metrics endpoint](http://localhost:8020/actuator/prometheus).  Notice that the application name forms part of each metric.

    **Part 3 - Setup Prometheus Configuration:**

1. Open the lab-4a/prometheus folder.  Open the `prometheus.yml` file found within.

1. The configuration is complete; notice the following:
    * The **services** job tells Prometheus to dynamically register clients found in Eureka. The "relabel" entry tells Prometheus to scrape the the actuator-provided endpoint rather than `/metrics` or some other path:

    ```
    - job_name: 'services'
      eureka_sd_configs:
      - server: http://host.docker.internal:8010/eureka
      relabel_configs:
      - target_label: __metrics_path__
        replacement: /actuator/prometheus  # relabel_configs:
    ```

    * To be complete, we should monitor the Prometheus, Grafana, and Eureka services themselves (Yes, Prometheus can scrape metrics from itself). Unfortunately, these cannot be easily implemented with service discovery, so we will hard-code these URLs:

    ```
    - job_name: 'eureka'
      metrics_path: '/actuator/prometheus'
      static_configs:
      - targets: ['host.docker.internal:8010']

    - job_name: 'prometheus-and-grafana'
      metrics_path: '/metrics' 
      static_configs:
      - targets: ['host.docker.internal:9090']
        labels:
          application: 'Prometheus'   
      - targets: ['host.docker.internal:3000']
        labels:
          application: 'Grafana'   
    ```      
    * Since our Eureka application is actually a Spring Boot application, prometheus metrics are available at `/actuator/prometheus`
    * The target values of `host.docker.internal` are used instead of `localhost`.  This is needed only because we will run Prometheus within a Docker container, and its definition of `localhost` references the container, not the underlying computer. (If install and run Prometheus without Docker, you would use `localhost`.) 
    * The `labels.application` will clearly delineate the Prometheus and Grafana metrics on the Grafana dashboard.  The Spring Boot-based applications do not require this as we configured them to emit an "application" tag on each metric.  Ultimately these will display at the top of the Grafana dashboard.

    **Part 4 - Run Prometheus:**

    > Note: Docker must be installed and running before performing these steps.

    > Note: If you are still running Prometheus from a previous lab, stop it now.

1. Open a command prompt relative to your `lab-4a` folder location.

1. Run the following command:
    * **Windows**
    ```
    docker run -p 9090:9090 -v %cd%/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
    ```

    * **Linux / Mac**
    ```
    docker run -p 9090:9090 -v $(pwd)/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
    ```

    * `docker run` Pulls and runs a Docker container from Docker Hub.
    * `-p 9090:9090` Maps port 9090 on localhost to the container's 9090.
    * `-v` Maps a volume / overrides a file.  We are overriding the default Prometheus configuration file located within the container with our own located outside the container.
    * `$(pwd)` or `%cd%` identifies the current directory on Linux or Windows.
    * `/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml` maps, or overlays, the containers prometheus.yml file with our own.
    * `prom/prometheus` is the actual container image for Prometheus that we are pulling from Docker Hub and running.

1. The Prometheus application should be running without errors at this point.

1. Open http://localhost:9090. You should see the Prometheus page.

1. Open http://localhost:9090/targets.  You should see entries for Prometheus, Grafana, Eureka, and most importantly our individual word and sentence services.  Each should be listed and in the "up" state - this may take a moment or two based on the scraping frequency.  

    **Part 5 - Configure and Run Grafana:**

    The Prometheus UI will let you query for specific metrics, but the tool of choice for this task is Grafana.

1. Open the lab-4a/grafana folder.  Open the `datasource.yml` file found within.

1. Notice that the datasource section is already completed.  It simply tells Grafana to obtain its information from the Prometheus server.

1. Open a command prompt relative to your `lab-4a` folder location.

    > Note: If you are still running Grafana from a previous lab, stop it now.

1. Run the following command:
    * **Windows**
    ```
    docker run -p 3000:3000 -v %cd%/grafana:/etc/grafana/provisioning/datasources grafana/grafana:9.5.2 
    ```

    * **Linux / Mac**
    ```
    docker run -p 3000:3000 -v $(pwd)/grafana:/etc/grafana/provisioning/datasources grafana/grafana:9.5.2 
    ```

    * `docker run` Pulls and runs a Docker container from Docker Hub.
    * `-p 3000:3000` Maps port 3000 on localhost to the container's 3000.
    * `-v` Maps a volume / overrides a file.  We are overriding Grafana's `/datasources` folder with our own.
    * `$(pwd)` or `%cd%` identifies the current directory on Linux or Windows.
    * `grafana/grafana:9.5.2` is the actual container image for Grafana that we are pulling from Docker Hub and running.  This is the latest stable release at the time these instructions were written, you may need to adjust the version in the future.

1. The Grafana application should be running without errors at this point.

1. Use a browser to open Grafana at http://localhost:3000.  Sign in with initial user and password values of **admin** and **admin**.  You will be prompted to change these values on the first signin -- remember the values you choose.

1. On the main Grafana web page, look for an option to create a dashboard.  

1. Look for an option to import a dashboard.  Import the `/grafana/Dashboard.json` file.  Select the Prometheus datasource and Import.

1. In the upper left corner of the dashboard, find a selection option for "application".  This will allow you to select between the two applications being scraped by the Prometheus server.

1. Feel free to experiment with this dashboard.  You can make the dashboard editable, then add "rows" and "visualizations" as you like.


    ### Reflection:

1. By integrating Prometheus with Eureka-based service discovery, our Spring Boot-based microservices will be scraped automatically, allowing for seamless reporting of metrics.

1. Our Spring Boot-based applications need very few adjustments to automatically emit metrics, only 1) actuator, 2) the micrometer dependency and 3) configuration to expose the prometheus endpoint.

1. The hard-coded URLs for Prometheus, Eureka, and Grafana are a bit of a disappointment, but in a real-world application we would probably establish DNS names for these common endpoints.

1. If we were hosting non-Spring Boot-based application in Eureka, we would need a more sophisticated configuration, as NodeJS, Python, .NET, and other apps would almost certainly not emit metrics via an `/actuator/prometheus` endpoint.  

1. Prometheus and Grafana are not Java based, therefore our dashboard does not display relevant information.  The solution here would be either multiple dashboards or a more intricate dashboard design.

1. Running Prometheus and Grafana within Docker requires us to make allowances when referencing other apps running on localhost.


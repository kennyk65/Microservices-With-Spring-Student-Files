## Lab 3a - Monitoring with Prometheus and Grafana

Before getting too far into this course, we should establish a good monitoring foundation.  Microservices can be quite a challenge to manage otherwise.

Spring Boot includes an __actuator__ dependency which can address many production-readiness needs, including monitoring.  However, metrics produced by a Spring Boot application must be collected by a separate system to be useful.  Many solutions are available, we will take a look at Prometheus and Grafana.

Prometheus came to prominence in the Kubernetes world, but it is a good general purpose technology describing a standard for emitting metrics, a server for collecting metrics, and a query language (PromQL).  Its graphing and alerting capabilities are a bit slim, which is why many use Grafana to finish the work done by Prometheus.

In this lab you will run Prometheus and Grafana servers (using Docker), then configure the config server and client from the last lab to emit metrics.

This lab requires Docker.


**Part 1 - Alter the Config Server:**

1. Open the lab-3a/server project.  This is essentially the solution from lab 3.

1. Open the pom.xml file.

1. **TODO-01**: Add the following dependencies:
    ```
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        
        <dependency>
          <groupId>io.micrometer</groupId>
          <artifactId>micrometer-registry-prometheus</artifactId>
          <scope>runtime</scope>
        </dependency>		
    ```
    * Spring Boot Actuator contains many features useful to applications in a production environment.  In this lab we will focus on metrics.
    * Since Spring Boot v2, actuator metrics are based on Micrometer.  This dependency allows Micrometer to produce Prometheus-formatted metrics. 

1.  Save your work.

    >  If using IntelliJ, the Maven extension may require you to update your project at this point.  From the menu, View / Maven / Refresh all...

    >  If using Eclipse, the M2E plugin may require you to update your project at this point.  Right click on the project / Maven / Update Project

1. Open `src/main/resources/application.yml`.

1. **TODO-02:** Add the following entries to the file.

    ```
    # Expose Actuator endpoint for Prometheus:
    management:
      endpoints.web.exposure.include: prometheus
      metrics:
        tags:
          application: ${spring.application.name}
    ```
    * The `exposure.include` entry tells Spring Boot to expose a `/actuator/prometheus` endpoint listing all collected metrics.  Later a prometheus server will "scrape" this endpoint to collect metrics.
      * You can experiment with other endpoints too, such as `health, info, beans, env, metrics, configprops, mappings, ` etc.  Place the desired endpoints in a comma-separated string.
    * The `metrics.tags.application` will include this application's name in the emitted metrics.  This will be valuable later when we have many microservices running.

1. Save your work.

1. Open `src/main/java/demo.Application`.  Run the application.
    * The application should run without errors.  If not, stop and review the previous steps.

1. Open a web browser.  Open http://localhost:8001/actuator/prometheus .  You should see several prometheus-formatted metrics listed.

    **Part 2 - Alter the Client:**

    Next you will perform the same essential steps for the client.

1. Open the lab-3a/client project.  Open the pom.xml file.

1. **TODO-03**: Add the following dependencies:
    ```
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        
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

1. **TODO-04:** Add the following entries to the file.

    ```
    # Expose Actuator endpoint for Prometheus:
    management:
      endpoints.web.exposure.include: prometheus
      metrics:
        tags:
          application: ${spring.application.name}
    ```

1. Save your work.

1. Open `src/main/java/demo.Application`.  Run the application.
    * The application should run without errors.  If not, stop and review the previous steps.

1. Open a web browser.  Open http://localhost:8002/actuator/prometheus .  You should see several prometheus-formatted metrics listed.

    **Part 3 - Setup Prometheus Configuration:**

1. Open the lab-3a/prometheus folder.  Open the `prometheus.yml` file found within.

1. Notice the following:
    * This configuration file will govern the Prometheus server's operations.
    * `scrape interval / timeout` controls how often Prometheus will pull metrics from your Spring Boot applications.
    * `scrape_configs` define details specific to various apps.
    * `job_name` is an arbitrary / descriptive value.
    * `metrics_path` defines the path where prometheus-formatted metrics can be found.  Previously we found them at **server:port/actuator/prometheus** 
    * `static_configs` allows us to list out the various applications which we want Prometheus to monitor.
    * `targets` lists the various URLs.  Notice this is an array, making it possible for a single app to have multiple running copies.
    * The target values of `host.docker.internal` are used instead of `localhost`.  This is needed only since we will run Prometheus within a Docker container, and its definition of `localhost` references the container, not the underlying computer. (If install and run Prometheus without Docker, you would use `localhost`.)
    * Optionally, `labels.application` provides a display name for this application.  In our case, we configured our monitored applications to emit an "application" tag on each metric, so we don't need to identify each application here.  Ultimately this will display at the top of the Grafana dashboard.

    **Part 4 - Run Prometheus:**

    > Note: Docker must be installed and running before performing these steps.

1. Open a command prompt relative to your `lab-3a` folder location.

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

1. Open http://localhost:9090/targets.  You should see your **Client** and **Config Server** applications listed and in the "up" state - this may take a moment or two based on the scraping frequency.  

    **Part 5 - Configure and Run Grafana:**

    The Prometheus UI will let you query for specific metrics, but the tool of choice for this task is Grafana.

1. Open the lab-3a/grafana folder.  Open the `datasource.yml` file found within.

1. **TODO-05**: Add a datasource section to this file.  Within create a datasouce with the name "Prometheus" having type "prometheus". Set the access type to "proxy".  Set the URL to point to the prometheus server launched earlier.  The URL should use the http protocol and reference "host.docker.internal:9090" rather than localhost. Set "isDefault" to true to make this the default datasource.

    ```
    datasources:
    - name: Prometheus
        type: prometheus
        access: proxy
        url: http://host.docker.internal:9090
        isDefault: true
    ```
    * Once again, "host.docker.internal" is used instead of localhost only because Grafana will be running within a Docker container.
1. Save your work.

1. Open a command prompt relative to your `lab-3a` folder location.

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

    **Part 6 (Optional) - Implement a custom metric:**

    Standard system metrics such as CPU and memory usage have value, but these are best when combined with business-specific metrics.  Spring Boot / Micrometer make it easy to add custom metrics which will automatically flow to Prometheus and Grafana.

    In this next section, we will add a simple custom metric to the client to count how many times the lucky word is rendered.

1. Return to lab-3a/client project.  Open `src/main/java/demo.CustomMetrics`.

1. **TODO-06:** Add a private final member variable of type `MeterRegistry`.  Add a constructor to inject this value:

    ```
    private final MeterRegistry meterRegistry;

    // Inject the MeterRegistry to register metrics
    public CustomMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    ```

1. **TODO-07:** Add a `@PostConstruct` method to instantiate the existing `LuckyWordRequests` metric counter.  Call the `meterRegistry`'s `counter()` method to create the new counter. Provide it with the name "custom.lucky-word-requests.counter".

    ```
    @PostConstruct
    public void initMetrics() {
        LuckyWordRequests = meterRegistry.counter("custom.lucky-word-requests.counter");
    }
    ```

1.  **TODO-08**: Create a public void `incrementLuckyWordMetric()` method.  Within, have it call the `increment()` method on the `LuckyWordRequests` counter.
    
    ```
    // Method to increment the metric
    public void incrementLuckyWordMetric() {
        LuckyWordRequests.increment();
    }
    ```

1. Open `src/main/java/demo.LuckyWordController`.

1. **TODO-09**: Before returning the lucky word, add code to call the `metrics.incrementLuckyWordMetric()`:

    ```
	metrics.incrementLuckyWordMetric();
	return "The lucky word is: " + luckyWord;
    ```
1. Save your work.  Restart the client application if it does not restart automatically.

1. Open a browser tab to http://localhost:8002/lucky-word .  Refresh the page a few times to generate some metrics.

    **Part 7 (Optional) - View the custom metric in Grafana**

1. Return to Grafana.  Open the dashboard you imported earlier.

1. Make the dashboard editable; Find a "Gear" icon near the top right, switch the dashboard to "editable", and save.

1. Return to the dashboard.  Find the "Add" button at the top of the screen.  Add a "row".

1. Change the title of the new "row" added to the dashboard.  Change it to "Lucky Word Data" or something similar.

1. Use the "Add" button again to add a "Visualization".

1. Find the option near the bottom to enter "code" (vs "builder").  Add the following PromQL expression:

    ```
    rate(custom_lucky_word_requests_counter_total[1m])
    ```
    * Notice the similarities and differences with the metric name you specified in code earlier.
    * See the [PromQL Cheat Sheet](https://promlabs.com/promql-cheat-sheet/) to learn more about this query language.
1. Click the "Run Queries" button.  The top part of the page will display a graph of the sum of lucky word requests.

1.  Adjust the "Panel Title" if you like, it is found on the panel on the right.

1.  Use the "Save" button to save your work.  Click the Dashboard's link found on the top of the page.  Move the visualization panel into the "row" you made earlier.


    ### Reflection:

1. Our Spring Boot applications need very few adjustments to automatically emit metrics, only 1) actuator, 2) the micrometer dependency and 3) configuration to expose the prometheus endpoint.

1. Prometheus and Grafana are relatively easy to install using Docker.  The trick is to override key files such as `prometheus.yml` or Grafana's datasources with customized versions.

1. Running Prometheus and Grafana within Docker requires us to make allowances when referencing other apps running on localhost.

1. Prometheus requires explicit configuration detailing the applications to "scrape".  In the next section, we will replace this explicit configuration with automated service discovery.

1. Micrometer and Spring Boot make it relatively easy to create custom metrics.  Grafana allows us to display these metrics.
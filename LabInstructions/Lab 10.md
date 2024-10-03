## Lab 10 - Securing the Spring Cloud Gateway with HTTP Basic

In this lab, we will add HTTP Basic security and HTTPS to the solution produced in the previous lab.  Spring Cloud Gateway will enforce HTTP Basic authentication for any user to use the application.

  **Part 1 - Startup**

1.  Stop ALL of the services that you may have running from previous exercises.  If using an IDE you may also wish to close all of the projects that are not related to "lab-10”.

1.  Start the common-config-server and common-eureka-server.  

1.  Lab 10 has copies of the word server.  Start 5 separate copies of the **lab-10/word-server**, using the profiles "subject", "verb", "article", "adjective", and "noun".  There are several ways to do this, depending on your preference:

    - If you wish to use Maven, open separate command prompts in the target directory and run these commands:
      - mvn spring-boot:run -Dspring.profiles.active=subject
      - mvn spring-boot:run -Dspring.profiles.active=verb
      - mvn spring-boot:run -Dspring.profiles.active=article
      - mvn spring-boot:run -Dspring.profiles.active=adjective
      - mvn spring-boot:run -Dspring.profiles.active=noun

    - If you wish to build the code and run the JAR, run `mvn package` in the project's root.  Then open separate command prompts in the target directory and run these commands:
      - java -jar -Dspring.profiles.active=subject   lab-10-word-server-1.jar 
      - java -jar -Dspring.profiles.active=verb      lab-10-word-server-1.jar 
      - java -jar -Dspring.profiles.active=article   lab-10-word-server-1.jar 
      - java -jar -Dspring.profiles.active=adjective lab-10-word-server-1.jar 
      - java -jar -Dspring.profiles.active=noun      lab-10-word-server-1.jar 

    - **IntelliJ** Open lab-10/word-server.  
      * Use menu "Run" / "Edit Configurations".  
      * Press "+" to add new configuration. Select "Application".  
      * Choose Name=noun, Main class=demo.Application.  
      * Click "Modify Options" / "Add VM Options".  
      * Enter `-Dspring.profiles.active=noun` in new field.
      * Apply.  Run.  
      * Repeat this process (or copy the run configuration) for the profiles "verb", "article", "adjective", "noun".

    - **Eclipse/STS** Import lab-10/word-server into your workspace.
      * R-click on the project, Run As... / Run Configurations... .
      * From the Spring Boot tab specify a Profile of "subject", 
      * UNCHECK JMX port / live bean support, and Run.  
      * Repeat this process (or copy the run configuration) for the profiles "verb", "article", "adjective", "noun".

1.  Check [Eureka](http://localhost:8010).   Any warnings about running a single instance are expected.  Ensure that each of your 5 applications are eventually listed in the "Application" section, bearing in mind it may take a few moments for the registration process to be 100% complete.	

1.  Optional - If you wish, you can click on the link to the right of any of these servers.  Replace the "actuator/info" with "/" and refresh several times.  You can observe the randomly generated words.  

1.  In a separate IDE, open **lab-10/sentence-server**.  Run this application.  Access it at [http://localhost:8088](http://localhost:8088).  
    * Expect to encounter errors in the page at this point.  The JavaScript / AJAX calls in the page have no knowledge of service discovery or the actual whereabouts of the word servers.

1.  In a separate IDE, open **lab-10/gateway**.  Run this application.  Access it at [http://localhost:8080](http://localhost:8080).  
    * Expect to see a web page with fully formed sentences containing random words.  This is the state of the application as of the conclusion of lab 9.  Next we will add HTTP Basic security.

    **Part 2 - Add HTTP Basic Security to the Gateway** 

    One advantage of the Gateway pattern is its ability to provide a central authentication point.  We can easily enforce HTTP Basic Authentication here.

1. Return to **lab-10/gateway**. Open the pom.xml.  

1. **TODO-01:** Add the dependency for Spring Boot Security:

    ```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    ```

1. Open application.yml.  

1. **TODO-02:** Add a name and password under the `spring.security.user` key:

    ```
    spring:
    
      # Other existing configuration...        

      security.user:
        name: admin
        password: password
    ```
    * Note that there are already several properties defined under the "spring" hierarchy. Make sure that the "security.user" tags are indented underneath "spring" properly.

1. Save your work.  Allow the application to restart (or restart it yourself). 

1. Open the gateway at [http://localhost:8080](http://localhost:8080).  You should be prompted for a userid and password.  Enter the values you defined above and observe the generated sentence.
    * Note: If you do not see a login page, you may need to open the gateway link in an incognito window.

    **Part 3 - Defining multiple users**

1. Return to application.yml.  Remove (or comment out) the user and password you defined earlier.

1. Open `src/main/java/demo.SecurityConfig`.  

1. **TODO-03:** Remove the comments from the `@Bean` method.  This method will override Spring Boot's default security configuration with a custom bean.  Remove the comments from the associated `import` statements at the top of the class.

1. **TODO-04:** Within the `userDetails()` method, add two new users, using the existing `UserDetails` definition as a guide. Use whatever values you like for username, password, and role.

1. **TODO-05:** Add your newly defined users to the `UserDetailsService` being returned from this method.

1. Save your work.  Allow the application to restart (or restart it yourself). 

1. Open the gateway at [http://localhost:8080](http://localhost:8080).  You should be prompted for a userid and password.  Enter one of the values you defined above and observe the generated sentence.
    * You may need to use an incognito browser window.

    **Part 4 - Add HTTPS**

    The use of HTTP Basic presumes a secure network.  We can make some small changes to our Gateway to have it implement HTTPS (with a self-signed certificate.)

1. Open a command prompt relative to **lab-10/gateway**'s `src/main/resources/` folder.  Run the following command (assuming Java is on the path):     

    ```
    keytool -genkeypair -alias testkey -keyalg RSA -keysize 2048 
    -dname "CN=SentenceGateway,OU=MicroservicesWithSpringCloud,O=Organization,L=Orlando,S=FL,C=US" 
    -keystore mykeystore.p12 -storepass secret -storetype PKCS12 -validity 3650
    ```

    * **keytool** Java utility for managing keystores and certificates. It allows you to generate key pairs, export certificates, import them, and handle certificate chains.
    * **-genkeypair** tells keytool to generate a public-private key pair. A key pair is made up of two keys: the public key (which can be shared with others) and the private key (which should be kept secure).
    * **-alias testkey** provides the name for the key pair being generated. In this case, the alias is testkey. You can use this alias to refer to the key pair later when you want to access or manage it in the keystore.
    * **-keyalg RSA** defines the key algorithm. Here, you are using the RSA algorithm, which is a widely used asymmetric encryption algorithm for public and private keys.
    * **-keysize 2048** sets the size (in bits) of the key being generated. In this case, it's a 2048-bit RSA key. This is a common and secure size for RSA keys.
    * **-dname** sets the Distinguished Name for the entity associated with this key pair. This is used to identify the entity and consists of multiple fields:
        * **CN (Common Name)** refers to a service, application, or server name.
        * **OU (Organizational Unit)** is typically the department or unit within the organization.
        * **O (Organization)** the organization name.
        * **L (Locality)** city where the entity is located.
        * **S (State)** state or province abbreviation.
        * **C (Country):** The country code.
    * **-keystore mykeystore.p12** specifies the file where the generated key pair will be stored. The .p12 extension indicates that this is a PKCS12 (Public-Key Cryptography Standards #12) keystore. PKCS12 is a binary format for storing a keystore (containing private keys and certificates) and is widely used for compatibility across different platforms.
    * **-storepass secret** password required to access the keystore.
    * **-storetype PKCS12** specifies the type of keystore being created or managed. PKCS12 is a standard format for storing cryptographic objects such as private keys and certificates. It is widely supported across different platforms and languages.  Can be used across various systems making it easier to share keys and certificates.
    * **-validity 3650** specifies the validity period of the generated key pair, measured in days. 3650 is approximately 10 years.

1.  Open `application.yml`.  

1. **TODO-06:** Add the following settings:

    ```
    server:
      port: 8443
      ssl:
        key-store: "classpath:mykeystore.p12"
        key-store-password: "secret"
    ```

1. Save your work.  Allow the application to restart (or restart it yourself). 

1. Open the gateway at [https://localhost:8443](https://localhost:8443).  
    * The browser should warn you about the website's certificate. Proceed to the site.
    * You should be prompted for a userid and password.  Enter one of the values you defined above and observe the generated sentence.
    * You may need to use an incognito browser window.
    
    **Reflection**

1. Adding HTTPS makes it possible to use HTTP Basic.  HTTP Basic provides a minimum level of authentication restriction on the application's use.

1. If we wanted to, we could experiment with authorization, for example making it so certain roles could not see articles or adjectives in sentences.

1. Because Spring Cloud Gateway is based on the Reactive stack, our security configuration required the use of reactive types, i.e. `MapReactiveUserDetailsService` vs `InMemoryUserDetailsManager`.

1. Storing users and passwords in code is cumbersome.  Placing these in a database would be a marginal improvement.  Ultimately, the API Gateway should not be burdened with the task of user management at all; it would be best to take the next step and move to an authentication scheme based on OAuth 2, SAML, LDAP, or some other scheme.

1. At this point, the sentence and word servers are completely insecure, relying on all calling traffic to pass through the gateway.  We can add another layer of security by having all service-to-service communications use HTTP Basic or a similar solution.

1. You may wonder if it is accurate to call the implemented solution "HTTP Basic" since the Gateway collected user and password through a login page.  However, remember the browser is making calls to the API Gateway to retrieve individual words, and these calls all use HTTP Basic.

1. We could override the look and feel of the login page if we like, but we will probably override this entire approach with OAuth 2 or SAML.
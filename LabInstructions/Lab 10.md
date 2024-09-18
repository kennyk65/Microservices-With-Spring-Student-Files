## Lab 10 - Applying Security to Cloud Applications

Prerequisite - If using a JDK earlier than 9, to use the encryption and decryption features you need the full-strength JCE installed in your JVM (it’s not there by default). You can download the "Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files" from Oracle, and follow instructions for installation (essentially replace the 2 policy files in the JRE lib/security directory with the ones that you downloaded).

  **Part 1 - Startup**

1.  Stop ALL of the services that you may have running from previous exercises.  If using an IDE you may also wish to close all of the projects that are not related to "lab-10”.

1.  Open **lab-10/config-server** and run it.  

1.  Open **lab-10/client** and run it.  

1.  Access the client at [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word) to see the lucky word.  This is essentially the result of what we achieved in Lab 3.




    **Part 2 - Securing the Config Server**

1.  Stop both services.

1.  Return to **lab-10/config-server**.  Open the POM, add another dependency for Spring Security (`org.springframework.boot`, `spring-boot-starter-securityspring-cloud-security`).

    >  If using IntelliJ, the Maven extension may require you to update your project at this point.  From the menu, View / Maven / Refresh all...

    >  If using Eclipse, the M2E plugin may require you to update your project at this point.  Right click on the project / Maven / Update Project

1.  Create a new Java class in the src/main/java/demo package named `SecurityConfig`.  Add the following method to define a filter chain:

    ```
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .authorizeHttpRequests(
                    authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())           // Needed to allow POST to /encrypt to work
                .httpBasic(Customizer.withDefaults() ); // Use basic authentication
            return http.build();
        }

    ```

    > Note: This should not be required as Spring Security will automatically configure HTTP Basic authentication and require that all requests are authenticated.  However, testing of this lab demonstrated that POSTs to /encrypt used later in this lab would not work unless CSRF were disabled.  Future software releases may address this.

1.  Open the application.yml in the classpath root (src/main/resources).  Add a key for `encrypt.key`.  Use any value you like (such as an innocuous value like `mykey`).

1.  Save your work and run the lab-10-config-server.  Observe the console output and obtain the generated password.  Copy it.

1.  Access the config server at [http://localhost:8001/anyapp-anyprofile.yml](http://localhost:8001/anyapp-anyprofile.yml).  You should be prompted for a user and password.  Enter “user” then the generated value for password. You should receive some YAML output. If so, you have successfully enabled HTTP Basic security and encryption in the config server.

    **Part 3 - Encrypt**

1.  Encrypt a password to be used by the config server:  Using a REST client, or the “curl” command on linux/unix, POST the string “password” (or some other some password value) to http://localhost:8001/encrypt.  You’ll have to provide the same “user” and generated value as in the last step.  Copy the encrypted returned value.

    ```
    curl -u user:GENERATED-PASSWORD-HERE -X POST http://localhost:8001/encrypt -d "password" -i
    ```

1.  Open application.yml.  Add a key for `spring.security.user.password`.  For the value, paste the encrypted password value from the last step, but prefix it with “{cipher}” (no quotes).  Then place the entire value within single quotes.  Example:

    ```
    spring:
      security.user:
        password: '{cipher}57db6NEWLY5aa5c8ENCRYPTEDc48daVALUE9e8109'
    ```

1.  Save your work.

1.  Restart the config server.  Notice that the console no longer contains a generated password.  Access [http://localhost:8001/anyapp-anyprofile.yml](http://localhost:8001/anyapp-anyprofile.yml).  You should be prompted for a user and password.  Enter “user” and “password” (or the value you encrypted for password).  You should once again see the YAML output.  If so, you have successfully configured the server with an encrypted password.

    **Part 4 - Adjust Client**

1.  The client won’t work until we adjust it to use the userid and password required by the config server.  Open **lab-10/client**.  

1.  Open application.yml.  Alter the spring.cloud.config.uri to include the userid and password.  The syntax is http://(USER):(PASSWORD)@localhost:8001 (without parenthesis).

1.  Start the client.  Access the client at [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word) to see the lucky word.  The client is now using HTTP basic authentication when accessing the config server.


    **BONUS - More Encryption**

1.  Encrypt a lucky word:  Using a REST client or curl command, POST a lucky word such as “Irish” to http://localhost:8001/encrypt.  Copy the encrypted returned value.

1.  Stop the config server.  Open application.yml.  Change the `spring.cloud.config.server.git.uri` to your own personal git repository.  If you are not sure what this is, take a look back at lab 3 or 8 where we used spring cloud config earlier.

1.  Open you repository’s copy of lucky-word-client.yml.  Change the “luckyWord” to the encrypted value from the step above.  Be sure to prefix it with “{cipher}” and enclose the entire value in single quotes.

1.  Start the config server.  Restart the client.  Access the client at [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word) to see the lucky word.  At this point, the word is stored in encrypted form, and is unencrypted by the config server before being sent to the client.

**Reflection**

1.  At present, all HTTP traffic is transmitted in the clear because we are not using HTTPS.  We could easily switch all of our apps to HTTPS, but doing so would require a certificate signed by a certificate authority.

1.  Requiring authenticated access to the config server improves security, but only to an extent.  It prevents casual network access to the config server.  However, if someone has access to the config server's configuration files, the backing encryption key can be obtained and the password can be decrypted and exploited.

1.  The encryption provided by the config server is a great way to avoid storing credentials in the repository in decrypted form.  But it is not foolproof; if one has access to the config server configuration properties, the encrypt.key can be obtained and used to decrypt the stored values.

1.  HTTP Basic authentication on the config server is nice, but it requires all clients to have a userid password, and this is stored in the clear.  It also requires effort to rotate the credentials.

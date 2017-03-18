##Lab 10 - Applying Security to Cloud Applications

Prerequisite - To use the encryption and decryption features you need the full-strength JCE installed in your JVM (it’s not there by default). You can download the "Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files" from Oracle, and follow instructions for installation (essentially replace the 2 policy files in the JRE lib/security directory with the ones that you downloaded).

  **Part 1 - Startup**

1.  Stop ALL of the services that you may have running from previous exercises.  If using an IDE you may also wish to close all of the projects that are not related to "lab-10”.

2.  Run the lab-10-config-server.  Run the lab-10-client.  Access the client at [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word) to see the lucky word.  This is essentially the result of what we achieved in Lab 3.


  **Part 2 - Securing the Config Server**

3.  Stop both services.

4.  Open lab-10-config-server.  Open the POM, add another dependency for spring-cloud-security.

5.  Create a bootstrap.yml in the classpath root (src/main/resources).  Add a key for “encrypt.key”.  Use any value you like (such as an obscure value like “key”).

6.  Save your work and run the lab-10-config-server.  Observe the console output and obtain the generated password.  Copy it.

7.  Access the config server at [http://localhost:8001/anyapp-anyprofile.yml](http://localhost:8001/anyapp-anyprofile.yml).  You should be prompted for a user and password.  Enter “user” then the generated value for password. You should receive some YAML output. If so, you have successfully enabled HTTP Basic security and encryption in the config server.

  **Part 3 - Encrypt**

8.  Encrypt a password to be used by the config server:  Using a REST client, or the “curl” command on linux/unix, POST the string “password” (or some other some password value) to http://localhost:8001/encrypt.  You’ll have to provide the same “user” and generated value as in the last step.  Copy the encrypted returned value.

9.  Open bootstrap.yml.  Add a key for security.user.password.  For the value, paste the encrypted password value from the last step, but prefix it with “{cipher}” (no quotes).  Then place the entire value within single quotes.  Save your work.

10.  Restart the config server.  Access [http://localhost:8001/anyapp-anyprofile.yml](http://localhost:8001/anyapp-anyprofile.yml).  You should be prompted for a user and password.  Enter “user” and “password” (or the value you encrypted for password).  You should once again see the YAML output.  If so, you have successfully configured the server with an encrypted password.

  **Part 4 - Adjust Client**

11.  The client won’t work until we adjust it to use the userid and password required by the config server.  Open the lab-10-client.  

12.  Open bootstrap.yml.  Alter the spring.cloud.config.uri to include the userid and password.  The syntax is http://(USER):(PASSWORD)@localhost:8001

13.  Start the client.  Access the client at [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word) to see the lucky word.  The client is now using HTTP basic authentication when accessing the config server.


  **BONUS - More Encryption**

14.  Encrypt a lucky word:  Using a REST client or curl command, POST a lucky word such as “Irish” to http://localhost:8001/encrypt.  Copy the encrypted returned value.

15.  Stop the config server.  Open application.yml.  Change the spring.cloud.config.server.git.uri to your own personal git repository.  If you are not sure what this is, take a look back at lab 3 or 8 where we used spring cloud config earlier.

16.  Open you repository’s copy of lucky-word-client.yml.  Change the “luckyWord” to the encrypted value from the step above.  Be sure to prefix it with “{cipher}” and enclose the entire value in single quotes.

17.  Start the config server.  Restart the client.  Access the client at [http://localhost:8002/lucky-word](http://localhost:8002/lucky-word) to see the lucky word.  At this point, the word is stored in encrypted form, and is unencrypted by the config server before being sent to the client.

**Reflection**

1.  At present, all HTTP traffic is transmitted in the clear because we are not using HTTPS.  We could easily switch all of our apps to HTTPS, but doing so would require a certificate signed by a certificate authority.

2.  The encryption provided by the config server is a great way to avoid storing credentials in the repository in decrypted form.  But it is not foolproof; if one has access to the config server, or the config server properties, the encrypt.key can be obtained and used to decrypt the stored values.

3.  HTTP Basic authentication on the config server is nice, but it requires all clients to have a userid password, and this is stored in the clear.  

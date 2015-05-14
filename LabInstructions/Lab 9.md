

Lab 9 - Securing Services

Required installation of JCE Unlimited Strength Jurisdition Policy Files from Oracle:  http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html

PART 1 - HTTP Basic Security

1.  Stop all services running from previous labs.  Close all projects unrelated to Lab 9.

2.  Start the lab-9-config-server and lab-9-eureka-server.

3.  Open the lab-9-word-server project.  Open the POM, add the org.springframework.boot / spring-boot-starter-security dependency.

4.  Launch this application using the profile “subject”.  Monitor the console output and obtain the default security password.  Copy it to your clipboard.

5.  From your Eureka server at http://localhost:8010/, click on the link to your “subject” service.  (Remember, it ordinarily takes Eureka a moment to synchronize its state)  

6.  Remove any “/info” suffix from the URL and refresh the browser.  You should be prompted for a username and password.  Supply “user” for the username and the default security password for the password.  You should get a JSON response containing a randomly generated “subject” for our sentence.

PART 2 - Encrypted password

7.  A randomly generated password won’t be very useful at runtime, so enhance the lab-9-word-server to use a designated password.  Add a security.user.password property to the application.yml file (use YAML format) and define any password value that you like.

8.  Restart your word server, activating the “subject” profile as you did before.  Obtain the URL via Eureka as you did above (it will be assigned a different port each time it launches, so you cannot simply refresh the browser).  Supply “user” for the username, and your supplied password for the password value.  You should see the generated subject.

9.  A hard-coded password is not very secure, so enhance your word server with an encrypted password.



BONUS - @RefreshScope



server has to be a config server in order to have /encrypt and /decrypt endpoints.
dependency has to be spring cloud security, not spring boot security
you need the JCE full strength jars from oracle
you need to set a ‘key’ for any encryption to work encrypt.key doesn’t work, but post to /key is.



org.springframework.cloud< / spring-cloud-starter-security
org.springframework.security.oauth / spring-security-oauth2

oauth documentation:  http://oauth.net

Make Https:
Any boot app: 
Use the JDK KeyTool command to create a Java Key Store:
keytool -genkeypair -alias testkey -keyalg RSA -dname "CN=Web Server,OU=Unit,O=Organization,L=City,S=State,C=US" -keypass secret -keystore mykeystore.jks -storepass secret
Except use any value you like for alias, keypads, keystore, and storepass.
- Copy the resulting file into your project’s class path, such as under src/main/resources.
- Modify the section of you application.yml (or properties) with these properties:
server:
  port: 8443
  ssl:
    key-store: classpath:mykeystore.jks
    key-store-password: secret
    key-password: secret  
- Test by using the URL https://localhost:8443/lucky-word-client/default/ .  You can ignore the warning about the certificate being unsafe - in a real world application we would have purchased a real certificate from a legitimate certificate authority.
- BUT you can’t have your clients connect to this because it is self signed.
  



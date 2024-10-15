## Spring Authorization Server ##

This project implement a simple OAuth2 Authorization Server using the [Spring Auth Server](https://docs.spring.io/spring-authorization-server/reference/getting-started.html) project. It borrows (steals) heavily from Dave Syer's OAuth2 Server example provided as part of the Spring Cloud demos: https://github.com/spring-cloud-samples/authserver

Firstly this is a standard Spring Boot web app running on port 9000. You can log into it using hard-coded credentials ( **user**/**password**) hard-coded into the SecurityConfig class - alter as needed.

The server is hard-coded with a single client and secret, intended to align with the [OAuth2 Client Demo](https://github.com/kennyk65/spring-teaching-demos/tree/master/oauth2AuthClient) project. Client ID: **oidc-client** and client-secret: **{noop}doNotTell** ("noop" mean the values is stored unencrypted).  Multiple clients are possible, and dynamically adding and removing clients is possible, but out of scope for this demo.

This server exposes a few endpoints that the client will need to connect up with, depending on which part of the OAuth2 flow is happening:

* `/oauth/authorize` is the Authorization endpoint to obtain user approval for a token grant. On the client side this is set using `spring.security.oauth2.client.provider.<auth-server-name>.authorization-uri`.

* `/oauth/token` is the Token endpoint, for clients to acquire access tokens given a code obtained earlier. On the client side this is set with `spring.security.oauth2.client.provider.<auth-server-name>.tokenUri`.

* `/authserver/userinfo` is used to obtain "User Info". The client needs to know what this URL is in the `spring.security.oauth2.client.provider.<auth-server-name>.userInfoUri`.  A Spring Security OAuth client will automatically set this value.

This Auth Server issues tokens in the form of JWTs.  You can experiment with different roles / scopes if you want.

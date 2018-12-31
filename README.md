# kn-project-jhipster

It's a project responsible to manipulate an XML( JSM queue message) and send to a different destination( JSM topic message).
- Using ActiveMQ as a Broker and 3 modules, one as a service(jhipster-registry-4.0.6.war - Eureka Server), patroclos-gateway and patroclos-app.

How to Run

# Run applications.

 - jhipster-registry-4.0.6.war
 
 $ ./jhipster-registry-4.0.6.war --spring.security.user.password=admin 
 --jhipster.security.authentication.jwt.secret=my-secret-key-which-should-be-changed-in-production-and-be-base64-encoded 
 --spring.cloud.config.server.composite.0.type=native --spring.cloud.config.server.composite.0.search-locations=file:./central-config 
 --spring.profiles.active=dev

 
 - patroclos-gateway and patroclos-app
 
$ ./mvnw 

# Run a single test class (patroclos-app).

$ ./mvnw -Dtest=PatroclosIntegration test

$ ./mvnw -Dtest=PatroclosUnit test

Remember, in order to everything works well you have to be running in your environment a local ActiveMQ Broker to manage messages.
You can find one here:
http://activemq.apache.org/download.html
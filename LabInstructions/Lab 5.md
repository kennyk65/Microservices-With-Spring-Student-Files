

Lab 5 - Using Ribbon Clients

Note: This lab assumes you have already completed the previous exercise on service discovery with Eureka.  If you have not, you can still continue if you obtain the solution projects for lab 4, and use them as a starting point.


1.  Start the eureka server, and all of the “word” client applications from the previous exercise (they may already be running!)  Stop the “sentence” application if it is running.  If you converted the lab to use Spring Cloud Config server, be sure to start that too.

2.  Copy the “lab4sentence” project into a new “lab5sentence” project.

3.  Edit the lab5sentence project.  Modify the POM (or gradle file) to bring in the dependencies needed for Spring Cloud Netflix Ribbon.

4.  Modify the controller.


package com.melon.springBootExample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// mvnw spring-boot:run
// navigate to http://localhost:8080/hello to try it
// http://localhost:8080/hello?name=YourName will display YourName instead of world

// @SpringBootApplication TODO - Only one SpringBootApplication can exist
@RestController // tells Spring that this code describes an endpoint that should be made available over the web
public class HelloWorldExampleApp {

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldExampleApp.class, args);
	}

	@GetMapping("/hello") // tells Spring to use our hello() method to answer requests that get sent to the http://localhost:8080/hello
	// RequestParam is telling Spring to expect a name value in the request, but if it’s not there, it will use the word "World" by default.
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
      return String.format("Hello %s!", name);
    }
}

package com.melon.springBootTests;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * MockMvc comes from Spring Test and lets you, through a set of convenient builder classes,
 * send HTTP requests into the DispatcherServlet and make assertions about the result.
 * Note the use of @AutoConfigureMockMvc and @SpringBootTest to inject a MockMvc instance.
 * Having used @SpringBootTest, we are asking for the whole application context to be created.
 * An alternative would be to ask Spring Boot to create only the web layers of the context by
 * using @WebMvcTest. In either case, Spring Boot automatically tries to locate the main
 * application class of your application, but you can override it or narrow it down if
 * you want to build something different.
 */
// @SpringBootTest
// The actual port is configured automatically in the base URL for the TestRestTemplate
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HelloControllerTest {

	@Autowired
	private MockMvc mvc;
    @Autowired
    private TestRestTemplate template;

    // This test mocks the HTTP request cycle
	@Test
	public void getHello() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("Greetings from Spring Boot!")));
	}

    // You can also use Spring Boot to write a simple full-stack integration test
    @Test
    public void getHelloFullStack() throws Exception {
        ResponseEntity<String> response = template.getForEntity("/", String.class);
        assertThat(response.getBody()).isEqualTo("Greetings from Spring Boot!");
    }
}
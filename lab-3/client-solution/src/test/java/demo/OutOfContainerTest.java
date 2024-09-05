package demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Out-of-container test for the config client.
 * Verifies everything -- except for the call to the config server.
 * 
 * @author ken krueger
 */

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("local-test")	//	"Keep the 'NorthAmerica" profile from loading, keep the external config server from being called.
public class OutOfContainerTest {

	@Autowired WebApplicationContext spring;
	MockMvc mockMvc;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(spring).build();
	}
	
	@Test
	public void teamListRetrieved() throws Exception {

		//	Test if the client application is working with the exception of the call to the server.:
		mockMvc.perform(get("/lucky-word"))
			.andExpect(status().isOk())
			.andExpect(content().string("The lucky word is: localtest"))
			;
	}

	//	Load test properties to satisfy the lucky-word placeholder:
	@Configuration
	@Import(Application.class)
	@PropertySource("classpath:/localtest/test.properties")
	public static class Config {
	}

}

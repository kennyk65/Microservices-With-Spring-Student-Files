package demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Out-of-container test for the config server.
 * Verifies that the server serves up configuration when asked.
 * Uses "native" profile to obtain properties from local file system rather than GitHub.
 * 
 * @author ken krueger
 */

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("native")	//	"native" means use local classpath location rather than GitHub.
public class OutOfContainerTest {

	@Autowired WebApplicationContext spring;
	MockMvc mockMvc;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(spring).build();
	}
	
	@Test
	public void propertyLoadTest() throws Exception {

		//	To test if this config server is working, we will simulate a "testConfig" client
		//	calling to get properties for its default profile.  These configuration files 
		//	(application.yml and testConfig.yml) are on the classpath as the server is 
		//	running the 'native' profile:
		MvcResult result =
			mockMvc.perform(get("/testConfig-default.properties"))
			.andExpect(status().isOk())
			.andReturn() 
			;
		
		String returned = result.getResponse().getContentAsString();

		
		//	Check that the test values from the yml are present in the properties:
		assertThat(returned).contains("fromApplication:","applicationValue");
		assertThat(returned).contains("fromTestConfig:","testConfigValue");
	}
}

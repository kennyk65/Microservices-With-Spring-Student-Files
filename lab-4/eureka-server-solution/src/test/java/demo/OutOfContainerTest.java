package demo;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
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
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("native")	//	"native" means use local classpath location rather than GitHub.
public class OutOfContainerTest {

	@Autowired WebApplicationContext spring;
	MockMvc mockMvc;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(spring).build();
	}
	
	@Test
	public void eurekaStartupTest() throws Exception {

		//	Testing Eureka can be quite involved.  For a simple test, we will just ensure
		//	that the application starts, and that the main web page can display.
		MvcResult result =
			mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andReturn() 
			;
		
	}
}

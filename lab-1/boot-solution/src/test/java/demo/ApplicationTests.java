package demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * Out-of-container test for the Spring Boot app.
 * Ensures that we can retrieve team information.
 * Verifies the entire stack, from DispatcherServlet to DB, without running a server.
 * 
 * @author ken krueger
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ApplicationTests {

	@Autowired WebApplicationContext spring;
	MockMvc mockMvc;
	
	@Before
	public void setup() {
		//	Setup Spring MVC Test Framework mock object for out-of-container testing.
		//	If you've never used the MVC Test Framework, you should look into it!
		mockMvc = MockMvcBuilders.webAppContextSetup(spring).build();
	}
	
	@Test
	public void teamsRetrieve() throws Exception {

		//	Ensure that everything works when we do a GET for all teams:
		mockMvc.perform(get("/teams"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(1))	
		.andExpect(jsonPath("$[0].name").value("Globetrotters"))	
		.andExpect(jsonPath("$[0].location").value("Harlem"))	
		.andExpect(jsonPath("$[0].players[*].name", containsInAnyOrder("Dizzy","Buckets","Big Easy")))
		;
	}

	@Test
	public void teamRetrieve() throws Exception {

		//	Ensure that everything works when we do a GET for a specific team.
		mockMvc.perform(get("/teams/1"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(1))	
		.andExpect(jsonPath("$.name").value("Globetrotters"))	
		.andExpect(jsonPath("$.location").value("Harlem"))	
		.andExpect(jsonPath("$.players[*].name", containsInAnyOrder("Dizzy","Buckets","Big Easy")))
		;
	}

	@Test
	public void playerRetrieve() throws Exception {

		//	Ensure that everything works when we do a GET for a specific team.
		mockMvc.perform(get("/teams/1/players/2"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name").value("Buckets"))	
		;
	}

}

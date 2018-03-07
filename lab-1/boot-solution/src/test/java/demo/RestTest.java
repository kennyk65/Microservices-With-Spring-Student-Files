package demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Set up Out-of-container test for the REST output.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class RestTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc =
            MockMvcBuilders.webAppContextSetup(context).build();
    }
	
    /**
     * Perform a test of /teams.  
     * See if there is a guard named Buckets on the Harlem Globetrotters.
     */
	@Test
	public void testRestfulGet() throws Exception {
	  mockMvc.perform(get("/teams")
	    .accept(MediaType.APPLICATION_JSON))
	    .andExpect(content().contentTypeCompatibleWith("application/json"))
	    .andExpect(jsonPath("$..[?(@.location=='Harlem')].players[?(@.name=='Buckets')].position").value("Guard"))
	        ;
	}
}
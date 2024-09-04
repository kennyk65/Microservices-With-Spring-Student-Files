package demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Set up Out-of-container test for the REST output.
 */
@SpringBootTest
public class RestTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
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

    /**
     * OPTIONAL - Perform an XML-based version of the same test.
     */
    @Test
    //@Disabled
    public void testRestfulGetXml() throws Exception {
      mockMvc.perform(get("/teams")
        .accept(MediaType.TEXT_XML))
        .andExpect(content().contentTypeCompatibleWith("text/xml"))
        .andExpect(xpath("//*[location='Harlem']/players/players[ name='Buckets' ]/position").string("Guard"))
         ;
    }  
}
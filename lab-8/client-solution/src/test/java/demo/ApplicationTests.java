package demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = { "wordConfig.luckyWord=foo", "wordConfig.preamble=bar" })
@WebAppConfiguration
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

}

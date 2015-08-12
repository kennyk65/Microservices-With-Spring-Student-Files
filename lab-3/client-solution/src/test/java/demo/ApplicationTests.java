package demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
@WebAppConfiguration
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

	//	Load test properties to satisfy the lucky-word placeholder:
	@Configuration
	@Import(Application.class)
	@PropertySource("classpath:demo/test.properties")
	public static class Config {
		
	}
}

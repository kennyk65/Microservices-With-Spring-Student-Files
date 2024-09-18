package demo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("local-test")	//	"Keep the 'NorthAmerica" profile from loading, keep the external config server from being called.
public class IntegrationTests {

	@Autowired LuckyWordController controller;
	
	@Test
	public void contextLoads() {
		//	Ensure that the context loads, the properties are loaded, and the lucky word
		//	controller is correctly wired with the correct value:
		assertThat(controller.showLuckyWord()).isEqualTo("The lucky word is: localtest");
		//Assert.assertTrue("The lucky word is: testing".equals(controller.showLuckyWord()));
	}

	//	Load test properties to satisfy the lucky-word placeholder:
	@Configuration
	@Import(Application.class)
	@PropertySource("classpath:/localtest/test.properties")
	public static class Config {
		
	}
}

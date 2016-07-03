package demo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
public class IntegrationTests {

	@Autowired LuckyWordController controller;
	
	@Test
	public void contextLoads() {
		//	Ensure that the context loads, the properties are loaded, and the lucky word
		//	controller is correctly wired with the correct value:
		Assert.assertTrue("The lucky word is: testing".equals(controller.showLuckyWord()));
	}

	//	Load test properties to satisfy the lucky-word placeholder:
	@Configuration
	@Import(Application.class)
	@PropertySource("classpath:demo/test.properties")
	public static class Config {
		
	}
}

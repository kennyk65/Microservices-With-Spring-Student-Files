package demo;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;



@RestController
public class SentenceController {

	@Autowired DiscoveryClient client;
	private RestTemplate template = new RestTemplate();
	
	@GetMapping("/sentence")
	public String getSentence() {
	  return String.format("%s %s %s %s %s.",
		  getWord("LAB-4-SUBJECT"),
		  getWord("LAB-4-VERB"),
		  getWord("LAB-4-ARTICLE"),
		  getWord("LAB-4-ADJECTIVE"),
		  getWord("LAB-4-NOUN") );
	}

	public String getWord(String service) {
		URI uri = 
			client.getInstances(service)
				.stream()
				.findAny()
				.orElseThrow(() -> new RuntimeException("No " + service + " instances available"))
				.getUri()
				;
		return template.getForObject(uri,String.class);
	}

}

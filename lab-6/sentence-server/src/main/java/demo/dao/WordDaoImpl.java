package demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.client.RestTemplate;

import demo.domain.Word;


public abstract class WordDaoImpl implements WordDao {

	//	Inject the load-balancing RestTemplate:
	@Autowired  RestTemplate template;

	public abstract String getPartOfSpeech();
	
	public Word getWord() {
		String service = getPartOfSpeech();
		try {
			return template.getForObject("http://" + service, Word.class);
		} catch (Exception e ) {
			System.out.println("Error retrieving " + service + " Error: " + e.getMessage());
			return new Word("(unknown " + service + ")");
		}

	}
	
}

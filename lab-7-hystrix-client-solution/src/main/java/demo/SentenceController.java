package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;


@Controller
public class SentenceController {

	@Autowired LoadBalancerClient loadBalancer;
	@Autowired RestTemplate restTemplate;
	
	/**
	 * Display a small list of Sentences to the caller:
	 */
	@RequestMapping("/sentence")
	public @ResponseBody String getSentence() {
	  return 
		"<h3>Some Sentences</h3><br/>" +	  
		buildSentence() + "<br/><br/>" +
		buildSentence() + "<br/><br/>" +
		buildSentence() + "<br/><br/>" +
		buildSentence() + "<br/><br/>" +
		buildSentence() + "<br/><br/>"
		;
	}

	/**
	 * Assemble a sentence by gathering random words of each part of speech:
	 */
	@HystrixCommand(fallbackMethod="noSentence")
	public String buildSentence() {
		return  
			getWord("SUBJECT") + " "
			+ getWord("VERB") + " "
			+ getWord("ARTICLE") + " "
			+ getWord("ADJECTIVE") + " "
			+ getWord("NOUN") + "."
			;
	}

	/**
	 * Fallback sentence, when there is difficulty making remote calls
	 */
	public String noSentence() {
		return "Sorry, no sentence can be constructed at this time";
	}

	
	/**
	 * Obtain a random word for a given part of speech, where the part 
	 * of speech is indicated by the given service / client ID:
	 */
	public String getWord(String service) {
		ServiceInstance instance = loadBalancer.choose(service);
   		return (new RestTemplate()).getForObject(instance.getUri(),String.class);
	}
	
}

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
	
	@RequestMapping("/sentence")
	@HystrixCommand(fallbackMethod="noSentence")
	public @ResponseBody String getSentence() {
	  return 
		getWord("SUBJECT") + " "
		+ getWord("VERB") + " "
		+ getWord("ARTICLE") + " "
		+ getWord("ADJECTIVE") + " "
		+ getWord("NOUN") + "."
		;
	}

	public String getWord(String service) {
		ServiceInstance instance = loadBalancer.choose(service);
   		return (new RestTemplate()).getForObject(instance.getUri(),String.class);
//   		return restTemplate.getForObject("http://"+service+"/"+service,String.class);
	}
	
	public String noSentence() {
		return "Sorry, no sentence can be constructed at this time";
	}
}

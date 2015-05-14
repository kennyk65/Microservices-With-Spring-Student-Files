package demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.client.RestTemplate;

import demo.domain.Word;


public abstract class WordDaoImpl implements WordDao {

	@Autowired LoadBalancerClient loadBalancer;
	
	public abstract String getPartOfSpeech();
	
	public Word getWord() {
		ServiceInstance instance = loadBalancer.choose(getPartOfSpeech());
   		return (new RestTemplate()).getForObject(instance.getUri(),Word.class);
	}
	
}

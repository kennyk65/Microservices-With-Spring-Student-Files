package demo.dao;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import demo.domain.Word;

@FeignClient("NOUN")
public interface NounClient {

	@RequestMapping(value="/", method=RequestMethod.GET)
	Word getWord();
	
}

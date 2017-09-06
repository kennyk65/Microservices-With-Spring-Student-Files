package demo.dao;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import demo.domain.Word;

@FeignClient("NOUN")
public interface NounClient {

	@GetMapping("/")
	public Word getWord();
	
}

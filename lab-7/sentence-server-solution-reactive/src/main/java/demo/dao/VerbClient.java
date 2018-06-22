package demo.dao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import demo.domain.Word;

@FeignClient("VERB")
public interface VerbClient {

	@GetMapping("/")
	Word getWord();
	
}

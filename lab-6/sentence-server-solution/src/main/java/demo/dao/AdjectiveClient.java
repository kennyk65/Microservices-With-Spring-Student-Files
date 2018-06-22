package demo.dao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import demo.domain.Word;

@FeignClient("ADJECTIVE")
public interface AdjectiveClient {

	@GetMapping("/")
	public Word getWord();

	
	static class HystrixClientFallback implements AdjectiveClient {

		@Override

		public Word getWord() {

		return new Word();

		}	
	}	
}
